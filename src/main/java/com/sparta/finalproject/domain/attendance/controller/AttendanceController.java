package com.sparta.finalproject.domain.attendance.controller;

import com.sparta.finalproject.domain.attendance.dto.AbsentAddRequestDto;
import com.sparta.finalproject.domain.attendance.service.AttendanceService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.agent.builder.AgentBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 등원 처리
    @PutMapping("manager/child/{childId}/enter")
    public GlobalResponseDto enterStatusModify(@PathVariable Long childId) {
        return attendanceService.modifyEnterStatus(childId);
    }

    // 하원 처리
    @PutMapping("manager/child/{childId}/exit")
    public GlobalResponseDto exitStatusModify(@PathVariable Long childId) {
        return attendanceService.modifyExitStatus(childId);
    }

    // 해당 반의 월별 출결 내역 조회
    @GetMapping("classroom/{classroomId}/attendance/month")
    public GlobalResponseDto attendanceMonthList(@PathVariable Long classroomId, @RequestParam int year,
                                                 @RequestParam int month) {
        return attendanceService.findAttendanceMonth(classroomId, year, month);
    }

}
