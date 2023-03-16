package com.sparta.finalproject.domain.classroom.service;

import com.sparta.finalproject.domain.classroom.dto.ClassroomRequestDto;
import com.sparta.finalproject.domain.classroom.dto.ClassroomResponseDto;
import com.sparta.finalproject.domain.classroom.dto.TeacherResponseDto;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.entity.Teacher;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.classroom.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;

    public String createClassroom(ClassroomRequestDto classroomRequestDto) {
        Classroom classroom = new Classroom(classroomRequestDto.getName());
        classroomRepository.saveAndFlush(classroom);
        return "반이 생성되었습니다.";
    }

    public ResponseEntity<ClassroomResponseDto> getClassroom(Long classroomId) {
        Classroom found = classroomRepository.findById(classroomId).orElseThrow(
                () -> new IllegalArgumentException("반을 찾을 수 없습니다.")
        );
        Optional<Teacher> classroomTeacher = teacherRepository.findByClassroom(found);
        if(classroomTeacher.isEmpty()){
            return ResponseEntity.ok(ClassroomResponseDto.of(classroomId));
        }
        return ResponseEntity.ok().body(ClassroomResponseDto.of(TeacherResponseDto.of(classroomTeacher.get()), classroomId));
    }
}
