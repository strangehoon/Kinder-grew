package com.sparta.finalproject.domain.child.controller;

import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.child.service.ChildService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;

    //아이 프로필 생성
    @PostMapping("api/managers/classes/{classroomId}/children")
    public GlobalResponseDto create(@PathVariable Long classroomId, @RequestBody ChildRequestDto childRequestDto) {
        return childService.createChild(classroomId, childRequestDto);
    }

    //반별 아이들 목록 조회
    @GetMapping("api/common/classes/{classroomId}/children")
    public GlobalResponseDto readClassroomChildren(@PathVariable Long classroomId){
        return childService.getClassroomChildren(classroomId);
    }

    //반별 아이 프로필 조회
    @GetMapping("api/common/classes/{classroomId}/children_profiles/{childId}")
    public GlobalResponseDto profile(@PathVariable Long classroomId, @PathVariable Long childId) {
        return childService.getChildProfile(classroomId,childId);
    }

    //반별 아이 프로필 수정
    @PatchMapping("api/managers/classes/{classroomId}/children-profiles/{childId}")
    public GlobalResponseDto update(@PathVariable Long classroomId, @PathVariable Long childId, @RequestBody ChildRequestDto childRequestDto) {
        return childService.updateChild(classroomId,childId,childRequestDto);
    }

    //반별 아이 검색
    @GetMapping("api/common/classes/{classroomId}/children/search")
    public GlobalResponseDto search(@PathVariable Long classroomId, @RequestParam(value = "name",required = false) String name) {
        return childService.searchChild(classroomId,name);
    }
}