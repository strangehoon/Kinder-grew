package com.sparta.finalproject.domain.attendance.service;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.attendance.repository.AttendanceRepository;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.AttendanceException;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.finalproject.global.response.CustomStatusCode.CHILD_NOT_FOUND;
import static com.sparta.finalproject.global.response.CustomStatusCode.NOT_FOUND_ATTENDANCE;

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
        List<Child> children = childRepository.findAll();
        List<Attendance> attendanceList = new ArrayList<>();
        if(children.isEmpty())
            throw new ChildException(CHILD_NOT_FOUND);
        for(Child child : children){
            if(attendanceRepository.findByChildAndDate(child, LocalDate.now()).isEmpty())
                attendanceList.add(Attendance.from(child));
        }
        attendanceRepository.saveAll(attendanceList);
    }

    //등원 처리
    @Transactional
    public GlobalResponseDto modifyEnterStatus(Long childId){
        Attendance attendance = attendanceRepository.findByChildIdAndDate(childId, LocalDate.now()).orElseThrow(
                () -> new AttendanceException(NOT_FOUND_ATTENDANCE)
        );
        // 등원 처리
        if(attendance.getEnterTime() == null){
            attendance.enter(LocalTime.now());
            return GlobalResponseDto.from(CustomStatusCode.CHILD_ENTER_SUCCESS);
        }
        // 등원 처리 취소
        else {
            attendance.enter(null);
            return GlobalResponseDto.from(CustomStatusCode.CHILD_ENTER_CANCEL);
        }
    }

    // 하원 처리
    @Transactional
    public GlobalResponseDto modifyExitStatus(Long childId){
        Attendance attendance = attendanceRepository.findByChildIdAndDate(childId, LocalDate.now()).orElseThrow(
                () -> new AttendanceException(NOT_FOUND_ATTENDANCE)
        );
        // 하원 처리
        if(attendance.getExitTime() == null){
            attendance.exit(LocalTime.now());
            return GlobalResponseDto.from(CustomStatusCode.CHILD_EXIT_SUCCESS);
        }

        // 하원 처리 취소
        else {
            attendance.exit(null);
            return GlobalResponseDto.from(CustomStatusCode.CHILD_EXIT_CANCEL);
        }
    }

}
