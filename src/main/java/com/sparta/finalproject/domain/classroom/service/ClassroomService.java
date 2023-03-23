package com.sparta.finalproject.domain.classroom.service;

import com.sparta.finalproject.domain.child.dto.ChildResponseDto;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.classroom.dto.ClassroomRequestDto;
import com.sparta.finalproject.domain.classroom.dto.ClassroomResponseDto;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final TeacherRepository teacherRepository;
    private final ChildRepository childRepository;

    @Transactional
    public GlobalResponseDto classroomAdd(ClassroomRequestDto classroomRequestDto) {
        Classroom classroom = new Classroom(classroomRequestDto.getName());
        classroomRepository.saveAndFlush(classroom);
        return GlobalResponseDto.of(CustomStatusCode.ADD_CLASSROOM_SUCCESS,ClassroomResponseDto.of(classroom.getId()));
    }

    @Transactional(readOnly = true)
    public GlobalResponseDto classroomFind(Long classroomId) {
        Classroom found = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ClassroomException(CustomStatusCode.CLASSROOM_NOT_FOUND)
        );
        Optional<Teacher> teacher = teacherRepository.findByClassroom(found);
        List<Child> children = childRepository.findAllByClassroomId(classroomId);
        List<ChildResponseDto> responseDtoList = children.stream().map(ChildResponseDto::of).collect(Collectors.toList());
        if(teacher.isEmpty()){
            return GlobalResponseDto.of(CustomStatusCode.FIND_CLASSROOM_SUCCESS,
                    ClassroomResponseDto.of(classroomId, responseDtoList));
        }
        return GlobalResponseDto.of(CustomStatusCode.FIND_CLASSROOM_SUCCESS,
                ClassroomResponseDto.of(classroomId, TeacherResponseDto.of(teacher.get()), responseDtoList));
    }
}
