package com.sparta.finalproject.domain.classroom.controller;

import com.sparta.finalproject.domain.classroom.dto.TeacherRequestDto;
import com.sparta.finalproject.domain.classroom.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PutMapping("/api/managers/{classroom_id}/teacher-profiles")
    public String updateTeacherInfo(@PathVariable Long classroom_id, @RequestBody TeacherRequestDto teacherRequestDto){
        return teacherService.updateTeacherInfo(teacherRequestDto,classroom_id);
    }
}
