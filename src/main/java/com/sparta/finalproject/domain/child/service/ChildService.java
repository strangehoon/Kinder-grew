package com.sparta.finalproject.domain.child.service;


import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.child.dto.ChildResponseDto;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.child.repository.ChildRepository;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.classroom.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final ClassroomRepository classroomRepository;

    //반별 아이 생성
    @Transactional
    public ChildResponseDto create(Long classroomId, ChildRequestDto childRequestDto) {

        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 반이 존재하지 않습니다."));

        Child child = childRepository.save(Child.of(childRequestDto,classroom));
        return ChildResponseDto.of(child);
    }

    //반별 아이 프로필 선택 조회
    @Transactional
    public ChildResponseDto profile(Long classroomId, Long childId) {
        Child child = childRepository.findByClassroomIdAndId(classroomId,childId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 아이의 정보가 없습니다."));
        return ChildResponseDto.of(child);
    }

    //반별 아이 프로필 수정
    @Transactional
    public ChildResponseDto update(Long classroomId,Long childId, ChildRequestDto childRequestDto) {
        Child child = childRepository.findByClassroomIdAndId(classroomId,childId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 아이의 정보가 없습니다."));
        child.update(childRequestDto);
        return ChildResponseDto.of(child);
    }

    //반별 아이 한명 검색
    @Transactional(readOnly = true)
    public ChildResponseDto search(Long classroomId, String name) {

        Optional<Child> child = childRepository.findByClassroomIdAndName(classroomId, name);
        if(child.isEmpty()) {
            throw new IllegalArgumentException("해당하는 아이가 존재하지 않습니다");
        }
        return ChildResponseDto.of(child.get());
    }
}