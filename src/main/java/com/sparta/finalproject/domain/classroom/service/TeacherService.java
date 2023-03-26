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
import com.sparta.finalproject.infra.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;
    private final S3Service s3Service;

    @Transactional
    public GlobalResponseDto teacherModify(TeacherRequestDto teacherRequestDto, Long classroomId) throws IOException {
        Classroom classroomFound = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ClassroomException(CustomStatusCode.SET_TEACHER_INFO_FAIL)
        );
        String profileImageUrl = s3Service.upload(teacherRequestDto.getImage(), "profile-image");
        Optional<Teacher> teacherFound = teacherRepository.findByClassroomId(classroomId);
        if(teacherFound.isEmpty()){
            Teacher teacher = teacherRepository.save(Teacher.of(teacherRequestDto, classroomFound, profileImageUrl));
            return GlobalResponseDto.of(CustomStatusCode.MODIFY_TEACHER_SUCCESS,TeacherResponseDto.of(teacher));
        }
        teacherFound.get().update(teacherRequestDto,classroomFound,profileImageUrl);
        return  GlobalResponseDto.of(CustomStatusCode.MODIFY_TEACHER_SUCCESS,TeacherResponseDto.of(teacherFound.get()));
    }
}
