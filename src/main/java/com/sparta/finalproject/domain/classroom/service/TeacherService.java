package com.sparta.finalproject.domain.classroom.service;

import com.sparta.finalproject.domain.classroom.dto.TeacherRequestDto;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.entity.Teacher;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.classroom.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;

    public String updateTeacherInfo(TeacherRequestDto teacherRequestDto, Long classroomId) {
        Classroom found = classroomRepository.findById(classroomId).orElseThrow(
                () -> new IllegalArgumentException("반이 존재하지 않습니다.")
        );
        teacherRepository.saveAndFlush(Teacher.of(teacherRequestDto,found));
        return "담임 선생님 프로필이 설정되었습니다.";
    }
}
