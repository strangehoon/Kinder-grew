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

    //아이 생성
    @PostMapping("classroom/{classroomId}/child")
    public GlobalResponseDto addChild (@PathVariable Long classroomId,
                                      @RequestPart(value = "data") ChildRequestDto childRequestDto,
                                      @RequestPart(value = "file") MultipartFile multipartFile) throws IOException  {
        return childService.childAdd(classroomId, childRequestDto, multipartFile);
    }

    //반별 아이들 목록 조회
    @GetMapping("classroom/{classroomId}/children")
    public GlobalResponseDto findChildren (@PathVariable Long classroomId){
        return childService.childrenFind(classroomId);
    }

    //반별 아이 조회
    @GetMapping("classroom/{classroomId}/child/{childId}")
    public GlobalResponseDto findChild (@PathVariable Long classroomId, @PathVariable Long childId) {
        return childService.childFind(classroomId,childId);
    }

    //반별 아이 수정
    @PutMapping("classroom/{classroomId}/child/{childId}")
    public GlobalResponseDto modifyChild (@PathVariable Long classroomId,
                                         @PathVariable Long childId,
                                         @RequestPart(value = "data") ChildRequestDto childRequestDto,
                                         @RequestPart(value = "file") MultipartFile multipartFile) throws IOException{
        return childService.childModify(classroomId,childId,childRequestDto,multipartFile);
    }

    //반별 아이 검색
    @GetMapping("classroom/{classroomId}/children/search")
    public GlobalResponseDto findChildByName(@PathVariable Long classroomId, @RequestParam(value = "name",required = false) String name) {
        return childService.childFindByName(classroomId,name);
    }

    // 등하원 시간 설정
    @PutMapping("parent/child/{childId}/schedule")
    public GlobalResponseDto attendanceTimeUpdate(@PathVariable Long childId, @RequestBody AttendanceModifyRequestDto requestDto){
        return childService.updateAttendanceTime(childId,requestDto);
    }

}