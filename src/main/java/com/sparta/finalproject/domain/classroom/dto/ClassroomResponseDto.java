package com.sparta.finalproject.domain.classroom.dto;


import com.sparta.finalproject.domain.child.dto.ChildResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ClassroomResponseDto {
    private final Long classId;
    private final List<ChildResponseDto> children;
    private final Long childrenCount;

    @Builder
    private ClassroomResponseDto(Long classId,  List<ChildResponseDto> children, Long childrenCount){

        this.classId = classId;
        this.children = children;
        this.childrenCount = childrenCount;
    }

    public static ClassroomResponseDto of(Long classId){
        return ClassroomResponseDto.builder()
                .classId(classId)
                .build();
    }

    public static ClassroomResponseDto of(Long classId,List<ChildResponseDto> children, Long childrenCount){

        return ClassroomResponseDto.builder()
                .classId(classId)
                .children(children)
                .childrenCount(childrenCount)
                .build();
    }

}
