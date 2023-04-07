package com.sparta.finalproject.domain.child.controller;

import com.sparta.finalproject.domain.child.dto.AttendanceModifyRequestDto;
import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.child.service.ChildService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                                      @ModelAttribute ChildRequestDto childRequestDto) throws IOException {
        log.info(childRequestDto.getName());
        return childService.addChild(classroomId, childRequestDto);
    }

    //반별 아이들 목록 조회
    @GetMapping("classroom/{classroomId}/children")
    public GlobalResponseDto childrenFind(@PathVariable Long classroomId,
                                          @RequestParam(required = false, defaultValue = "1") int page) {
        return childService.findChildren(classroomId, page - 1);
    }

    //반별 아이 조회
    @GetMapping("classroom/{classroomId}/child/{childId}")
    public GlobalResponseDto childFind(@PathVariable Long classroomId, @PathVariable Long childId) {
        return childService.findChild(classroomId, childId);
    }

    //반별 아이 수정
    @PutMapping("classroom/{classroomId}/child/{childId}")
    public GlobalResponseDto childModify(@PathVariable Long classroomId,
                                         @PathVariable Long childId,
                                         @ModelAttribute ChildRequestDto childRequestDto) throws IOException {
        return childService.modifyChild(classroomId, childId, childRequestDto);
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

    // 관리자 페이지 조회
    @GetMapping("manager/classroom/{classroomId}")
    public GlobalResponseDto childScheduleFind(@RequestParam int page, @RequestParam int size, @PathVariable Long classroomId,
                                               @RequestParam String state, @RequestParam String time){
        page -= 1;
        return childService.findChildSchedule(page, size, classroomId, state, time);
    }

}