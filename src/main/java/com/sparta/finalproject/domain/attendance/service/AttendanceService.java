package com.sparta.finalproject.domain.attendance.service;

import com.sparta.finalproject.domain.attendance.dto.AbsentAddRequestDto;
import com.sparta.finalproject.domain.attendance.dto.AbsentAddResponseDto;
import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.attendance.repository.AttendanceRepository;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.finalproject.global.response.CustomStatusCode.CHILD_NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendanceService {
    private final ChildRepository childRepository;
    private final AttendanceRepository attendanceRepository;

    // 출결 테이블 자동 생성
    @Transactional
    @Scheduled(cron = "0 0 0 * * 1-5")
    public void addDailyAttendance(){
        List<Child> childList = childRepository.findAll();
        List<Attendance> attendanceList = new ArrayList<>();
        if(childList.isEmpty())
            throw new ChildException(CHILD_NOT_FOUND);
        for(Child child : childList){
            if(attendanceRepository.findByChildAndDate(child, LocalDate.now()).isEmpty())
                attendanceList.add(Attendance.from(child));
        }
        attendanceRepository.saveAll(attendanceList);
    }

    // 결석 신청
    @Transactional
    public GlobalResponseDto addAbsent(Long childId, AbsentAddRequestDto requestDto) {
        Child child = childRepository.findById(childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );
        LocalDate startDate = requestDto.getStartDate();
        LocalDate endDate = requestDto.getEndDate();
        List<Attendance> attendanceList = new ArrayList<>();
        Period period = Period.between(startDate, endDate);
        for(int i = 0; i < period.getDays()+1; i++){
            Attendance attendance = Attendance.of(child, startDate.plusDays(i), requestDto.getReason());
            attendanceList.add(attendance);
        }
        attendanceRepository.saveAll(attendanceList);
        return GlobalResponseDto.of(CustomStatusCode.CREATE_ABSENT_SUCCESS, AbsentAddResponseDto.of(startDate, endDate, requestDto.getReason()));
    }
}
