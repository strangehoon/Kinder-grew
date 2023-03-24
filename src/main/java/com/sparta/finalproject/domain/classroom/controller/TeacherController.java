package com.sparta.finalproject.domain.classroom.controller;

import com.sparta.finalproject.domain.classroom.dto.TeacherRequestDto;
import com.sparta.finalproject.domain.classroom.service.TeacherService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PutMapping("/api/managers/classes/{classroomId}/teacher-profiles")
    public GlobalResponseDto modifyTeacher(@PathVariable Long classroomId,
                                           @RequestPart (value = "data") TeacherRequestDto teacherRequestDto,
                                           @RequestPart (value = "file") MultipartFile multipartFile) throws IOException {
        return teacherService.teacherModify(teacherRequestDto,classroomId, multipartFile);
    }
}
