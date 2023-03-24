package com.sparta.finalproject.domain.classroom.controller;

import com.sparta.finalproject.domain.classroom.dto.ClassroomRequestDto;
import com.sparta.finalproject.domain.classroom.service.ClassroomService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping("api/manager/classes")
    public GlobalResponseDto addClassroom (@RequestBody ClassroomRequestDto classroomRequestDto){
        return classroomService.classroomAdd(classroomRequestDto);
    }

    @GetMapping("/api/common/classes/{classroomId}")
    public GlobalResponseDto findClassroom(@PathVariable Long classroomId){
        return classroomService.classroomFind(classroomId);
    }
}
