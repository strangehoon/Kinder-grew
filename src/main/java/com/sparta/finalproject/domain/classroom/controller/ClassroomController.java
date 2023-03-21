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
    public GlobalResponseDto createClassroom(@RequestBody ClassroomRequestDto classroomRequestDto){
        return classroomService.createClassroom(classroomRequestDto);
    }

    @GetMapping("/api/common/classes/{classroomId}")
    public GlobalResponseDto readClassroom(@PathVariable Long classroomId){
        return classroomService.getClassroom(classroomId);
    }
}
