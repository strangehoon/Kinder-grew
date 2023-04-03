package com.sparta.finalproject.domain.attendance.controller;

import com.sparta.finalproject.domain.attendance.dto.AbsentAddRequestDto;
import com.sparta.finalproject.domain.attendance.service.AttendanceService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 결석 신청
    @PostMapping("parent/child/{childId}/absent")
    public GlobalResponseDto absentAdd(@PathVariable Long childId, @RequestBody AbsentAddRequestDto requestDto) {
        return attendanceService.addAbsent(childId, requestDto);
    }

}
