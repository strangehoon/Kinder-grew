package com.sparta.finalproject.domain.classroom.controller;

import com.sparta.finalproject.domain.classroom.dto.TeacherRequestDto;
import com.sparta.finalproject.domain.classroom.service.TeacherService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PutMapping("/api/managers/{classroomId}/teacher-profiles")
    public GlobalResponseDto updateTeacherInfo(@PathVariable Long classroomId, @RequestBody TeacherRequestDto teacherRequestDto){
        return teacherService.updateTeacherInfo(teacherRequestDto,classroomId);
    }
}
