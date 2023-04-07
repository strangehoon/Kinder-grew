package com.sparta.finalproject.domain.classroom.service;

import com.sparta.finalproject.domain.child.dto.ChildResponseDto;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.classroom.dto.ClassroomRequestDto;
import com.sparta.finalproject.domain.classroom.dto.ClassroomResponseDto;
import com.sparta.finalproject.domain.classroom.dto.ClassroomTeacherResponseDto;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.domain.user.repository.UserRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ClassroomException;
import com.sparta.finalproject.global.response.exceptionType.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    private final ClassroomRepository classroomRepository;
    private final ChildRepository childRepository;
    private static final int CHILD_SIZE = 15;
    private final UserRepository userRepository;

    @Transactional
    public GlobalResponseDto classroomAdd(ClassroomRequestDto classroomRequestDto) {
        Classroom classroom = Classroom.from(classroomRequestDto.getName());
        classroomRepository.save(classroom);
        return GlobalResponseDto.of(CustomStatusCode.ADD_CLASSROOM_SUCCESS,ClassroomResponseDto.from(classroom.getId()));
    }

    @Transactional(readOnly = true)
    public GlobalResponseDto classroomFind(Long classroomId, int page) {
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                ()-> new ClassroomException(CustomStatusCode.CLASSROOM_NOT_FOUND)
        );
        Pageable pageable = PageRequest.of(page, CHILD_SIZE, Sort.by(Sort.Direction.ASC, "id"));
        Page<Child> children = childRepository.findAllByClassroomId(classroomId, pageable);
        Long childrenCount = childRepository.countByClassroomId(classroomId);
        List<ChildResponseDto> responseDtoList = children.stream().map(ChildResponseDto::of).collect(Collectors.toList());
        ClassroomTeacherResponseDto classroomTeacher = new ClassroomTeacherResponseDto(classroom.getClassroomTeacher());
        return GlobalResponseDto.of(CustomStatusCode.FIND_CLASSROOM_SUCCESS,
                ClassroomResponseDto.of(classroomId, responseDtoList, childrenCount, classroomTeacher));
    }

    @Transactional
    public GlobalResponseDto modifyClassroomTeacher(Long classroomId, Long userId, User user) {
        if(!user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new UserException(CustomStatusCode.UNAUTHORIZED_USER);
        }
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ClassroomException(CustomStatusCode.CLASSROOM_NOT_FOUND)
        );
        User classroomTeacher = userRepository.findById(userId).orElseThrow(
                () -> new UserException(CustomStatusCode.TEACHER_NOT_FOUND)
        );
        classroom.update(classroomTeacher);
        return GlobalResponseDto.of(CustomStatusCode.MODIFY_CLASSROOM_TEACHER_SUCCESS, null);
    }
}
