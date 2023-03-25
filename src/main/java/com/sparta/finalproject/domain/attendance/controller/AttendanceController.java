package com.sparta.finalproject.domain.attendance.controller;

import com.sparta.finalproject.domain.attendance.service.AttendanceService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("api/managers/attendance")
    public String addAttendance(){
        attendanceService.addDailyAttendance();
        return "성공";
    }

    @PatchMapping("api/managers/attendance/enter")
    public GlobalResponseDto modifyChildEnter(@RequestParam(value = "childId") Long childId) {
        return attendanceService.childEnterModify(childId);
    }

    @PatchMapping("api/managers/attendance/exit")
    public GlobalResponseDto modifyChildExit(@RequestParam(value = "childId") Long childId) {
        return attendanceService.childExitModify(childId);
    }

    @PatchMapping("api/managers/attendance/absent")
    public GlobalResponseDto modifyChildAbsent(@RequestParam(value = "childId") Long childId) {
        return attendanceService.childAbsentModify(childId);
    }
}
