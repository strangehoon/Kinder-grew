package com.sparta.finalproject.domain.classroom.controller;

import com.sparta.finalproject.domain.classroom.dto.TeacherRequestDto;
import com.sparta.finalproject.domain.classroom.service.TeacherService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://sparta-ys.shop",allowedHeaders = "*")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PutMapping("/api/managers/classes/{classroomId}/teacher-profiles")
    public GlobalResponseDto modifyTeacher(@PathVariable Long classroomId, @RequestBody TeacherRequestDto teacherRequestDto){
        return teacherService.teacherModify(teacherRequestDto,classroomId);
    }
}
