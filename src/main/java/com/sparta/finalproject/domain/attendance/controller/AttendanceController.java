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

}
