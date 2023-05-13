package com.sparta.finalproject.domain.child.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChildrenEnterResponseDto {
    private Long childrenCount;
    private List<ChildEnterResponseDto> childEnterResponseDtoList;

    @Builder
    private ChildrenEnterResponseDto(Long childrenCount, List<ChildEnterResponseDto> childEnterResponseDtoList){
        this.childrenCount = childrenCount;
        this.childEnterResponseDtoList = childEnterResponseDtoList;
    }

    public static ChildrenEnterResponseDto of(Long childrenCount, List<ChildEnterResponseDto> childEnterResponseDtoList){
        return ChildrenEnterResponseDto.builder()
                .childrenCount(childrenCount)
                .childEnterResponseDtoList(childEnterResponseDtoList)
                .build();
    }
}
