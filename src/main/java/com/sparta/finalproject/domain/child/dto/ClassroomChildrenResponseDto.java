package com.sparta.finalproject.domain.child.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ClassroomChildrenResponseDto {
    private Long childrenCount;
    private List<ChildResponseDto> childResponseDtoList;

    @Builder
    private ClassroomChildrenResponseDto(Long childrenCount, List<ChildResponseDto> childResponseDtoList){
        this.childrenCount = childrenCount;
        this.childResponseDtoList = childResponseDtoList;
    }

    public static ClassroomChildrenResponseDto of(Long childrenCount, List<ChildResponseDto> childResponseDtoList){
        return ClassroomChildrenResponseDto.builder()
                .childrenCount(childrenCount)
                .childResponseDtoList(childResponseDtoList)
                .build();
    }
}
