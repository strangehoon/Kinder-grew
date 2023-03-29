package com.sparta.finalproject.domain.attendance.service;

import com.sparta.finalproject.domain.attendance.dto.EnterResponseDto;
import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.attendance.repository.AttendanceRepository;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttendanceService {
    private final ChildRepository childRepository;
    private final AttendanceRepository attendanceRepository;
//    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");

    @Transactional
//    @Scheduled(cron = "0 0 0 * * 1-5")
    public void addDailyAttendance(){
        List<Child> children = childRepository.findAll();
        List<Attendance> attendanceList = new ArrayList<>();
        for (Child child : children){
            attendanceList.add(Attendance.of(child));
        }
        attendanceRepository.saveAll(attendanceList);
    }

    @Transactional
    public GlobalResponseDto childEnterModify(Long childId){
        Attendance childAttendance = attendanceRepository.findByChildIdAndDate(childId, LocalDate.now());
        childAttendance.enter();
        if(childAttendance.isEntered()){
            return GlobalResponseDto.of(CustomStatusCode.CHILD_ENTER_SUCCESS, EnterResponseDto.of(childAttendance));
        }
        return GlobalResponseDto.of(CustomStatusCode.CHILD_ENTER_CANCEL, EnterResponseDto.of(childAttendance));
    }

    @Transactional
    public GlobalResponseDto childExitModify(Long childId) {
        Attendance childAttendance = attendanceRepository.findByChildIdAndDate(childId, LocalDate.now());
        childAttendance.exit();
        if(childAttendance.isExited()){
            return GlobalResponseDto.of(CustomStatusCode.CHILD_EXIT_SUCCESS, EnterResponseDto.of(childAttendance));
        }
        return GlobalResponseDto.of(CustomStatusCode.CHILD_EXIT_CANCEL, EnterResponseDto.of(childAttendance));
    }

    @Transactional
    public GlobalResponseDto childAbsentModify(Long childId) {
        Attendance childAttendance = attendanceRepository.findByChildIdAndDate(childId, LocalDate.now());
        childAttendance.absented();
        if(childAttendance.isAbsented()){
            return GlobalResponseDto.of(CustomStatusCode.CHILD_ABSENT_SUCCESS, EnterResponseDto.of(childAttendance));
        }
        return GlobalResponseDto.of(CustomStatusCode.CHILD_ABSENT_CANCEL, EnterResponseDto.of(childAttendance));
    }
}
