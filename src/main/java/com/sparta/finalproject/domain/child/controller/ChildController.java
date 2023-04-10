package com.sparta.finalproject.domain.child.controller;

import com.sparta.finalproject.domain.child.dto.AttendanceModifyRequestDto;
import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.child.service.ChildService;
import com.sparta.finalproject.domain.security.UserDetailsImpl;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;

    //아이 생성
    @PostMapping("classroom/{classroomId}/child")
    public GlobalResponseDto childAdd(@PathVariable Long classroomId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @ModelAttribute ChildRequestDto childRequestDto) throws IOException {
        log.info(childRequestDto.getName());
        return childService.addChild(classroomId, childRequestDto, userDetails.getUser());
    }

    //반별 아이들 목록 조회
    @GetMapping("classroom/{classroomId}/children")
    public GlobalResponseDto childrenFind(@PathVariable Long classroomId,
                                          @RequestParam(required = false, defaultValue = "1") int page) {
        return childService.findChildren(classroomId, page - 1);
    }

    //반별 아이 조회
    @GetMapping("classroom/{classroomId}/child/{childId}")
    public GlobalResponseDto childFind(@PathVariable Long classroomId,
                                       @PathVariable Long childId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return childService.findChild(classroomId, childId, userDetails.getUser());
    }

    //아이 수정
    @PutMapping("classroom/{classroomId}/child/{childId}")
    public GlobalResponseDto childModify(@PathVariable Long classroomId,
                                         @PathVariable Long childId,
                                         @ModelAttribute ChildRequestDto childRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return childService.modifyChild(classroomId, childId, userDetails.getUser(), childRequestDto);
    }

    //반별 아이 검색
    @GetMapping("classroom/{classroomId}/children/search")
    public GlobalResponseDto childFindByName(@PathVariable Long classroomId, @RequestParam String name) {
        return childService.findChildByName(classroomId, name);
    }

    // 등하원 시간 설정
    @PutMapping("parent/child/{childId}/schedule")
    public GlobalResponseDto attendanceTimeModify(@PathVariable Long childId, @RequestBody AttendanceModifyRequestDto requestDto) {
        return childService.modifyAttendanceTime(childId, requestDto);
    }

    // 등하원 시간 조회
    @GetMapping("parent/child/{childId}/schedule")
    public GlobalResponseDto attendanceTimeFind(@PathVariable Long childId){
        return childService.findAttendanceTime(childId);
    }

    // 관리자 페이지 조회
    @GetMapping("manager/classroom/{classroomId}")
    public GlobalResponseDto childScheduleFind(@RequestParam int page, @RequestParam int size, @PathVariable Long classroomId,
                                               @RequestParam String state, @RequestParam String time) {
        return childService.findChildSchedule(page -1, size, classroomId, state, time);
    }
}