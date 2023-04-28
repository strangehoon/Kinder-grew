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
    @PostMapping("kindergarten/{kindergartenId}/classroom/{classroomId}/child")
    public GlobalResponseDto childAdd(@PathVariable Long kindergartenId, @PathVariable Long classroomId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @ModelAttribute ChildRequestDto childRequestDto) throws IOException {
        log.info(childRequestDto.getName());
        return childService.addChild(kindergartenId, classroomId, childRequestDto, userDetails.getUser());
    }

    //반별 아이들 목록 조회
    @GetMapping("kindergarten/{kindergartenId}/classroom/{classroomId}/children")
    public GlobalResponseDto childrenFind(@PathVariable Long classroomId, @PathVariable Long kindergartenId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestParam(required = false, defaultValue = "1") int page) {
        return childService.findChildren(classroomId, kindergartenId,page - 1, userDetails.getUser());
    }

    //반별 아이 조회
    @GetMapping("kindergarten/{kindergartenId}/classroom/{classroomId}/child/{childId}")
    public GlobalResponseDto childFind(@PathVariable Long kindergartenId,@PathVariable Long classroomId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @PathVariable Long childId) {
        return childService.findChild(kindergartenId, classroomId, childId, userDetails.getUser());
    }

    //아이 수정
    @PutMapping("kindergarten/{kindergartenId}/classroom/{classroomId}/child/{childId}")
    public GlobalResponseDto childModify(@PathVariable Long kindergartenId, @PathVariable Long classroomId,
                                         @PathVariable Long childId,
                                         @ModelAttribute ChildRequestDto childRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return childService.modifyChild(kindergartenId,classroomId, childId, childRequestDto, userDetails.getUser());
    }

    //반별 아이 검색
    @GetMapping("kindergarten/{kindergartenId}/classroom/{classroomId}/children/search")
    public GlobalResponseDto childFindByName(@PathVariable Long kindergartenId, @PathVariable Long classroomId, @RequestParam String name,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return childService.findChildByName(kindergartenId, classroomId, name, userDetails.getUser());
    }

    // 등하원 시간 설정
    @PutMapping("parent/child/{childId}/schedule")
    public GlobalResponseDto attendanceTimeModify(@PathVariable Long childId, @RequestBody AttendanceModifyRequestDto requestDto,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return childService.modifyAttendanceTime(childId, requestDto, userDetails.getUser());
    }

    // 등하원 시간 조회
    @GetMapping("parent/child/{childId}/schedule")
    public GlobalResponseDto attendanceTimeFind(@PathVariable Long childId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return childService.findAttendanceTime(childId, userDetails.getUser());
    }

    // 아이 등하원 현황 조회(관리자 페이지)
    @GetMapping("manager/kindergarten/{kindergartenId}/classroom/{classroomId}")
    public GlobalResponseDto childScheduleFind(@RequestParam int page, @RequestParam int size, @PathVariable Long classroomId, @PathVariable Long kindergartenId,
                                               @RequestParam String state, @RequestParam String time, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return childService.findChildSchedule(page -1, size, classroomId, kindergartenId, state, time, userDetails.getUser());
    }

    //학부모 페이지 아이 조회
    @GetMapping("parent/child/{childId}")
    public GlobalResponseDto parentPageChildFind(@PathVariable Long childId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return childService.findParentPageChild(childId,userDetails.getUser());
    }

    //학부모 페이지 아이 수정
    @PutMapping("parent/child/{childId}")
    public GlobalResponseDto parentPageChildModify(@PathVariable Long childId, ChildRequestDto childRequestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return childService.modifyParentPageChild(childId,childRequestDto,userDetails.getUser());
    }
}