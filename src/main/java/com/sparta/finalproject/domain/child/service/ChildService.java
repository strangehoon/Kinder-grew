package com.sparta.finalproject.domain.child.service;


import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.child.dto.ChildResponseDto;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.ChildException;
import com.sparta.finalproject.global.response.exceptionType.ClassroomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final ClassroomRepository classroomRepository;

    //반별 아이 생성
    @Transactional
    public GlobalResponseDto createChild(Long classroomId, ChildRequestDto childRequestDto) {
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));
        Child child = childRepository.save(Child.of(childRequestDto,classroom));
        return GlobalResponseDto.of(CustomStatusCode.CREATE_CHILD_SUCCESS,ChildResponseDto.of(child));
    }

    //반별 아이 프로필 선택 조회
    @Transactional
    public GlobalResponseDto getChildProfile(Long classroomId, Long childId) {
        Child child = childRepository.findByClassroomIdAndId(classroomId,childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));
        return GlobalResponseDto.of(CustomStatusCode.GET_CHILD_PROFILE_SUCCESS,ChildResponseDto.of(child));
    }

    //반별 아이 프로필 수정
    @Transactional
    public GlobalResponseDto updateChild(Long classroomId, Long childId, ChildRequestDto childRequestDto) {
        Child child = childRepository.findByClassroomIdAndId(classroomId,childId).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND));
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new ClassroomException(CustomStatusCode.CLASSROOM_NOT_FOUND)
        );
        child.update(childRequestDto, classroom);
        return GlobalResponseDto.of(CustomStatusCode.UPDATE_CHILD_SUCCESS,ChildResponseDto.of(child));
    }

    //반별 아이 한명 검색
    @Transactional(readOnly = true)
    public GlobalResponseDto searchChild(Long classroomId, String name) {
        Child child = childRepository.findByClassroomIdAndName(classroomId, name).orElseThrow(
                () -> new ChildException(CustomStatusCode.CHILD_NOT_FOUND)
        );
        return GlobalResponseDto.of(CustomStatusCode.SEARCH_CHILD_SUCCESS,ChildResponseDto.of(child));
    }

    public GlobalResponseDto getClassroomChildren(Long classroomId) {
        List<Child> children = childRepository.findAllByClassroomId(classroomId);
        List<ChildResponseDto> responseDtoList = children.stream().map(ChildResponseDto::of).collect(Collectors.toList());
        return GlobalResponseDto.of(CustomStatusCode.GET_CHILDREN_SUCCESS, responseDtoList);
    }
}