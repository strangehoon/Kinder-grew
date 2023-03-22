package com.sparta.finalproject.domain.child.controller;

import com.sparta.finalproject.domain.child.dto.AttendanceModifyRequestDto;
import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.child.service.ChildService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ChildController {
    private final ChildService childService;

    //아이 프로필 생성
    @PostMapping("api/managers/classes/{classroomId}/children")
    public GlobalResponseDto create(@PathVariable Long classroomId,
                                    @RequestPart(value = "data") ChildRequestDto childRequestDto,
                                    @RequestPart(value = "file") MultipartFile multipartFile) throws IOException  {
        return childService.createChild(classroomId, childRequestDto, multipartFile);
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
    public GlobalResponseDto update(@PathVariable Long classroomId,
                                    @PathVariable Long childId,
                                    @RequestPart(value = "data") ChildRequestDto childRequestDto,
                                    @RequestPart(value = "file") MultipartFile multipartFile) throws IOException{
        return childService.updateChild(classroomId,childId,childRequestDto,multipartFile);
    }

    //반별 아이 검색
    @GetMapping("api/common/classes/{classroomId}/children/search")
    public GlobalResponseDto search(@PathVariable Long classroomId, @RequestParam(value = "name",required = false) String name) {
        return childService.searchChild(classroomId,name);
    }

    // 등하원 시간 설정
    @PutMapping("api/parents/schedule/{childId}")
    public GlobalResponseDto attendanceTimeModify(@PathVariable Long childId, @RequestBody AttendanceModifyRequestDto requestDto){
        return childService.modifyAttendanceTime(childId,requestDto);
    }
}