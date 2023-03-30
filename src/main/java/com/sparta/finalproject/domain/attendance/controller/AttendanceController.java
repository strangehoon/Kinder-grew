package com.sparta.finalproject.domain.attendance.controller;

import com.sparta.finalproject.domain.attendance.service.AttendanceService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("api/managers/attendance")
    public String addAttendance(){
        attendanceService.addDailyAttendance();
        return "성공";
    }

    @PutMapping("manager/child/{childId}/enter")
    public GlobalResponseDto modifyChildEnter(@PathVariable Long childId) {
        return attendanceService.childEnterModify(childId);
    }

    @PutMapping("manager/child/{childId}/exit")
    public GlobalResponseDto modifyChildExit(@PathVariable Long childId) {
        return attendanceService.childExitModify(childId);
    }

//    @PutMapping("api/managers/attendance/absent")
//    public GlobalResponseDto modifyChildAbsent(@RequestParam(value = "childId") Long childId) {
//        return attendanceService.childAbsentModify(childId);
//    }
}
