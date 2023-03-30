//package com.sparta.finalproject.domain.attendance.controller;
//
//import com.sparta.finalproject.domain.attendance.service.AttendanceService;
//import com.sparta.finalproject.global.dto.GlobalResponseDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//public class AttendanceController {
//
//    private final AttendanceService attendanceService;
//
//    @PostMapping("api/managers/attendance")
//    public String addAttendance(){
//        attendanceService.addDailyAttendance();
//        return "성공";
//    }
//
//    // 등원 처리
//    @PutMapping("manager/child/{childId}")
//    public GlobalResponseDto childEnterModify(@PathVariable Long childId) {
//        return attendanceService.modifyChildEnter(childId);
//    }
//
//    // 하원 처리
//    @PatchMapping("api/managers/attendance/exit")
//    public GlobalResponseDto modifyChildExit(@RequestParam(value = "childId") Long childId) {
//        return attendanceService.childExitModify(childId);
//    }
//
//    @PatchMapping("api/managers/attendance/absent")
//    public GlobalResponseDto modifyChildAbsent(@RequestParam(value = "childId") Long childId) {
//        return attendanceService.childAbsentModify(childId);
//    }
//}
