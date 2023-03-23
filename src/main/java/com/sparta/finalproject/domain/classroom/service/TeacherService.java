package com.sparta.finalproject.domain.classroom.service;

import com.sparta.finalproject.domain.classroom.dto.TeacherRequestDto;
import com.sparta.finalproject.domain.classroom.dto.TeacherResponseDto;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.entity.Teacher;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.classroom.repository.TeacherRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ClassroomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;

    @Transactional
    public GlobalResponseDto teacherModify(TeacherRequestDto teacherRequestDto, Long classroomId) {
        Classroom found = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ClassroomException(CustomStatusCode.SET_TEACHER_INFO_FAIL)
        );
        Teacher teacher = teacherRepository.saveAndFlush(Teacher.of(teacherRequestDto, found));
        return GlobalResponseDto.of(CustomStatusCode.MODIFY_TEACHER_SUCCESS,TeacherResponseDto.of(teacher));
    }
}
