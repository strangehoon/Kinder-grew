package com.sparta.finalproject.domain.classroom.service;

import com.sparta.finalproject.domain.classroom.dto.ClassroomRequestDto;
import com.sparta.finalproject.domain.classroom.dto.ClassroomResponseDto;
import com.sparta.finalproject.domain.classroom.dto.TeacherResponseDto;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.entity.Teacher;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.classroom.repository.TeacherRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;

    @Transactional
    public GlobalResponseDto createClassroom(ClassroomRequestDto classroomRequestDto) {
        Classroom classroom = new Classroom(classroomRequestDto.getName());
        classroomRepository.saveAndFlush(classroom);
        return GlobalResponseDto.of(CustomStatusCode.CREATE_CLASSROOM_SUCCESS,ClassroomResponseDto.of(classroom.getId()));
    }

    @Transactional(readOnly = true)
    public GlobalResponseDto getClassroom(Long classroomId) {
        Classroom found = classroomRepository.findById(classroomId).orElseThrow(
                () -> new IllegalArgumentException("반을 찾을 수 없습니다.")
        );
        Optional<Teacher> classroomTeacher = teacherRepository.findByClassroom(found);
        if(classroomTeacher.isEmpty()){
            return GlobalResponseDto.of(CustomStatusCode.GET_CLASSROOM_SUCCESS,ClassroomResponseDto.of(classroomId));
        }
        return GlobalResponseDto.of(CustomStatusCode.GET_CLASSROOM_SUCCESS,
                ClassroomResponseDto.of(TeacherResponseDto.of(classroomTeacher.get()), classroomId));
    }
}
