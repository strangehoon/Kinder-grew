package com.sparta.finalproject.domain.classroom.dto;


import com.sparta.finalproject.domain.child.dto.ChildResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ClassroomResponseDto {
    private final Long classId;
    private final TeacherResponseDto teacher;
    private final List<ChildResponseDto> children;

    @Builder
    private ClassroomResponseDto(Long classId,TeacherResponseDto teacher,  List<ChildResponseDto> children){
        this.classId = classId;
        this.teacher = teacher;
        this.children = children;
    }

    public static ClassroomResponseDto of(Long classId){
        return ClassroomResponseDto.builder()
                .classId(classId)
                .build();
    }

    public static ClassroomResponseDto of(TeacherResponseDto teacher, Long classId){
        return ClassroomResponseDto.builder()
                .teacher(teacher)
                .classId(classId)
                .build();
    }

    public static ClassroomResponseDto of(List<ChildResponseDto> children) {
        return ClassroomResponseDto.builder()
                .children(children)
                .build();
    }

    public static ClassroomResponseDto of(Long classId,List<ChildResponseDto> children){
        return ClassroomResponseDto.builder()
                .classId(classId)
                .children(children)
                .build();
    }

    public static ClassroomResponseDto of(Long classId, TeacherResponseDto teacher, List<ChildResponseDto> children){
        return ClassroomResponseDto.builder()
                .classId(classId)
                .teacher(teacher)
                .children(children)
                .build();
    }


}
