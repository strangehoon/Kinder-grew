package com.sparta.finalproject.domain.attendance.controller;

import com.sparta.finalproject.domain.attendance.dto.AbsentAddRequestDto;
import com.sparta.finalproject.domain.attendance.dto.AbsentCancelRequestDto;
import com.sparta.finalproject.domain.attendance.service.AttendanceService;
import com.sparta.finalproject.domain.security.UserDetailsImpl;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.exceptionType.AttendanceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // 등원 처리
    @PutMapping("manager/child/{childId}/enter")
    public GlobalResponseDto enterStatusModify(@PathVariable Long childId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               HttpServletResponse response) {
        return attendanceService.modifyEnterStatus(childId, userDetails.getUser());
    }

    // 하원 처리
    @PutMapping("manager/child/{childId}/exit")
    public GlobalResponseDto exitStatusModify(@PathVariable Long childId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return attendanceService.modifyExitStatus(childId, userDetails.getUser());
    }

    // 해당 반의 월별 출결 내역 조회
    @GetMapping("classroom/{classroomId}/attendance/month")
    public GlobalResponseDto attendanceMonthList(@PathVariable Long classroomId, @RequestParam int year,
                                                 @RequestParam int month, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return attendanceService.findAttendanceMonth(classroomId, year, month, userDetails.getUser());
    }

    // 반 별 해당 날짜의 출결 내역 조회
    @GetMapping("classroom/{classroomId}/attendance/day")
    public GlobalResponseDto attendanceDayList(@PathVariable Long classroomId, @RequestParam String date,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return attendanceService.findAttendanceDate(classroomId, date, userDetails.getUser());
    }

    // 결석 신청
    @PostMapping("parent/child/{childId}/absent")
    public GlobalResponseDto absentAdd(@PathVariable Long childId, @RequestBody AbsentAddRequestDto absentAddRequestDto,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) throws AttendanceException {
        return attendanceService.addAbsent(childId, absentAddRequestDto, userDetails.getUser());
    }


    //결석 취소
    @DeleteMapping("parent/child/{childId}/absent/{absentInfoId}")
    public GlobalResponseDto absentCancel(@PathVariable Long childId, @PathVariable Long absentInfoId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return attendanceService.cancelAbsent(childId, absentInfoId, userDetails.getUser());
    }

    // 자녀의 월별 출결 내역 조회
    @GetMapping("parent/child/{childId}/attendance/month")
    public GlobalResponseDto childAttendanceMonthList(@PathVariable Long childId, @RequestParam int year,
                                                      @RequestParam int month, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return attendanceService.findChildAttendanceMonth(childId, year, month, userDetails.getUser());
    }
}
