package com.sparta.finalproject.domain.classroom.controller;

import com.sparta.finalproject.domain.classroom.dto.ClassroomRequestDto;
import com.sparta.finalproject.domain.classroom.dto.ClassroomResponseDto;
import com.sparta.finalproject.domain.classroom.service.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping("api/manager/classes")
    public String createClassroom(@RequestBody ClassroomRequestDto classroomRequestDto){
        return classroomService.createClassroom(classroomRequestDto);
    }

    @GetMapping("/api/common/classes/{classroom_id}")
    public ResponseEntity<ClassroomResponseDto> readClassroom(@PathVariable Long classroom_id){
        return classroomService.getClassroom(classroom_id);
    }
}
