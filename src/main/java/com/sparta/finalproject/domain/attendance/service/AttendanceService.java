package com.sparta.finalproject.domain.attendance.service;

import com.sparta.finalproject.domain.attendance.dto.DateAttendanceResponseDto;
import com.sparta.finalproject.domain.attendance.dto.DayAttendanceResponseDto;
import com.sparta.finalproject.domain.attendance.dto.MonthAttendanceResponseDto;
import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.attendance.repository.AttendanceRepository;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.Day;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.AttendanceException;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.finalproject.global.enumType.AttendanceStatus.*;
import static com.sparta.finalproject.global.enumType.Day.*;
import static com.sparta.finalproject.global.response.CustomStatusCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendanceService {
    private final ChildRepository childRepository;
    private final AttendanceRepository attendanceRepository;

    // 출결 테이블 자동 생성
    @Transactional
    @Scheduled(cron = "0 0 0 * * 1-6")
    public void addDailyAttendance(){
        List<Child> children = childRepository.findAll();
        List<Attendance> attendanceList = new ArrayList<>();
        if(children.isEmpty())
            throw new ChildException(CHILD_NOT_FOUND);
        for(Child child : children){
            if(attendanceRepository.findByChildAndDate(child, LocalDate.now()).isEmpty())
                attendanceList.add(Attendance.of(child, 미등원));
        }
        attendanceRepository.saveAll(attendanceList);
    }

    // 23시 59분에 미등원이면 결석처리, 나머지는 출석으로 처리
    @Transactional
    @Scheduled(cron = "0 59 23 * * 1-6")
    public void handleDailyAttendance(){
        List<Child> children = childRepository.findAll();
        if(children.isEmpty())
            throw new ChildException(CHILD_NOT_FOUND);
        for(Child child : children){
            Attendance attendance = attendanceRepository.findByChildIdAndDate(child.getId(), LocalDate.now()).orElseThrow(
                    () -> new AttendanceException(NOT_FOUND_ATTENDANCE)
            );
            if(attendance.getStatus()==미등원){
                attendance.update(결석, null, null, "무단결석");
            }
            else if(attendance.getStatus()==하원){
                attendance.update(출석);
            }
            else{
                attendance.update(출석, LocalTime.parse("20:00:00"));
            }
        }
    }


    //등원 처리
    @Transactional
    public GlobalResponseDto modifyEnterStatus(Long childId){
        Attendance attendance = attendanceRepository.findByChildIdAndDate(childId, LocalDate.now()).orElseThrow(
                () -> new AttendanceException(NOT_FOUND_ATTENDANCE)
        );
        // 등원 처리
        if(attendance.getEnterTime() == null){
            attendance.enter(LocalTime.now(), 등원);
            return GlobalResponseDto.from(CustomStatusCode.CHILD_ENTER_SUCCESS);
        }
        // 등원 처리 취소
        else {
            attendance.enter(null, 미등원);
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
            attendance.exit(LocalTime.now(), 하원);
            return GlobalResponseDto.from(CustomStatusCode.CHILD_EXIT_SUCCESS);
        }

        // 하원 처리 취소
        else {
            attendance.exit(null, 등원);
            return GlobalResponseDto.from(CustomStatusCode.CHILD_EXIT_CANCEL);
        }
    }

    // 해당 반의 월별 출결 내역 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findAttendanceMonth(Long classroomId, int year, int month){
        List<Child> children = childRepository.findAllByClassroomId(classroomId);
        List<MonthAttendanceResponseDto> monthAttendanceList = new ArrayList<>();
        for(Child child : children){
            List<DayAttendanceResponseDto> dayAttendanceList = new ArrayList<>();
            List<Attendance> attendanceList = attendanceRepository.findAttendanceListByMonth(year, month, child.getId());
            for(Attendance attendance : attendanceList){
                Day day = getDay(attendance);
                dayAttendanceList.add(DayAttendanceResponseDto.of(attendance, day));
            }
            List<Attendance> enteredAttendance = attendanceRepository.findByStatusAndChildIdAndMonth(출석, child.getId(), month);
            int attendanceCount = enteredAttendance.size();
            List<Attendance> absentedAttendance = attendanceRepository.findByStatusAndChildIdAndMonth(결석, child.getId(), month);
            int absentedCount = absentedAttendance.size();
            monthAttendanceList.add(MonthAttendanceResponseDto.of(child, dayAttendanceList, attendanceCount, absentedCount));
        }
        return GlobalResponseDto.of(MONTH_ATTENDANCE_LIST_SUCCESS, monthAttendanceList);

    }

    // 반 별 해당 날짜의 출결 내역 조회
    @Transactional(readOnly = true)
    public GlobalResponseDto findAttendanceDate(Long classroomId, String date){
        List<Child> children = childRepository.findAllByClassroomId(classroomId);
        List<DateAttendanceResponseDto> attendanceResponseDtoList = new ArrayList<>();
        for(Child child : children){
            DateAttendanceResponseDto dateAttendanceResponseDto = childRepository.findDateAttendance(LocalDate.parse(date), child.getId());
            attendanceResponseDtoList.add(dateAttendanceResponseDto);
        }
        return GlobalResponseDto.of(DATE_ATTENDANCE_LIST_SUCCESS, attendanceResponseDtoList);
    }


    private static Day getDay(Attendance attendance) {
        Day day = null;
        switch(attendance.getDate().getDayOfWeek().getValue()) {
            case 1:
                day = 월;
                break;
            case 2:
                day = 화;
                break;
            case 3:
                day = 수;
                break;
            case 4:
                day = 목;
                break;
            case 5:
                day = 금;
                break;
            case 6:
                day = 토;
                break;
            case 7:
                day = 일;
                break;
        }
        return day;
    }

}
