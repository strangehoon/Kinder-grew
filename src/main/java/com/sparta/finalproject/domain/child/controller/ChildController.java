package com.sparta.finalproject.domain.child.controller;

import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.child.dto.ChildResponseDto;
import com.sparta.finalproject.domain.child.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;

    //반별 아이 추가
    @PostMapping("api/managers/classes/{classroomId}/children")
    public ChildResponseDto create(@PathVariable Long classroomId, @RequestBody ChildRequestDto childRequestDto) {
        return childService.create(classroomId, childRequestDto);
    }

    //반별 아이 프로필 조회
    @GetMapping("api/common/classes/{classroomId}/children_profiles/{childId}")
    public ChildResponseDto profile(@PathVariable Long classroomId, @PathVariable Long childId) {
        return childService.profile(classroomId,childId);
    }

    //반별 아이 프로필 수정
    @PatchMapping("api/managers/classes/{classroomId}/children-profiles/{childId}")
    public ChildResponseDto update(@PathVariable Long classroomId, @PathVariable Long childId, @RequestBody ChildRequestDto childRequestDto) {
        return childService.update(classroomId,childId,childRequestDto);
    }

    //반별 아이 검색
    @GetMapping("api/common/classes/{classroomId}/children/search")
    public ChildResponseDto search(@PathVariable Long classroomId, @RequestParam(value = "name",required = false) String name) {
        return childService.search(classroomId,name);
    }
}