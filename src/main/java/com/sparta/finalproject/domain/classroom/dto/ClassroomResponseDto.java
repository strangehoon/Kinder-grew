package com.sparta.finalproject.domain.classroom.dto;


import com.sparta.finalproject.domain.child.dto.ChildResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ClassroomResponseDto {
    private final Long classroomId;
    private final List<ChildResponseDto> children;
    private final Long childrenCount;
    private final ClassroomTeacherResponseDto classroomTeacher;

    @Builder
    private ClassroomResponseDto(Long classroomId, List<ChildResponseDto> children, Long childrenCount, ClassroomTeacherResponseDto classroomTeacher){
        this.classroomId = classroomId;
        this.children = children;
        this.childrenCount = childrenCount;
        this.classroomTeacher = classroomTeacher;
    }

    public static ClassroomResponseDto from(Long classroomId){
        return ClassroomResponseDto.builder()
                .classroomId(classroomId)
                .build();
    }

    public static ClassroomResponseDto of(Long classroomId, List<ChildResponseDto> children, Long childrenCount){
        return ClassroomResponseDto.builder()
                .classroomId(classroomId)
                .children(children)
                .childrenCount(childrenCount)
                .build();
    }

    public static ClassroomResponseDto of(Long classroomId, List<ChildResponseDto> children, Long childrenCount, ClassroomTeacherResponseDto classroomTeacher){
        return ClassroomResponseDto.builder()
                .classroomId(classroomId)
                .children(children)
                .childrenCount(childrenCount)
                .classroomTeacher(classroomTeacher)
                .build();
    }

}
