package com.sparta.finalproject.domain.classroom.controller;

import com.sparta.finalproject.domain.classroom.dto.TeacherRequestDto;
import com.sparta.finalproject.domain.classroom.service.TeacherService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PutMapping("manager/classroom/{classroomId}/teacher/profile")
    public GlobalResponseDto modifyTeacher(@PathVariable Long classroomId,
                                           @ModelAttribute TeacherRequestDto teacherRequestDto) throws IOException {
        return teacherService.teacherModify(teacherRequestDto,classroomId);
    }
}
