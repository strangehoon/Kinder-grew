package com.sparta.finalproject.domain.classroom.controller;

import com.sparta.finalproject.domain.classroom.dto.ClassroomRequestDto;
import com.sparta.finalproject.domain.classroom.service.ClassroomService;
import com.sparta.finalproject.domain.security.UserDetailsImpl;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @PostMapping("classroom")
    public GlobalResponseDto classroomAdd(@RequestBody ClassroomRequestDto classroomRequestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return classroomService.addClassroom(classroomRequestDto, userDetails.getUser());
    }

    @GetMapping("classroom/{classroomId}")
    public GlobalResponseDto classroomFind(@PathVariable Long classroomId, @AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestParam(required = false, defaultValue = "1") int page){
        return classroomService.findClassroom(classroomId, userDetails.getUser(), page-1);
    }

    @PutMapping("classroom/{classroomId}/classroom_teacher/{teacherId}")
    public GlobalResponseDto classroomTeacherModify(@PathVariable Long classroomId,
                                                    @PathVariable Long teacherId,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails){
        return classroomService.modifyClassroomTeacher(classroomId, teacherId, userDetails.getUser());
    }
}
