package com.sparta.finalproject.domain.child.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChildrenExitResponseDto {
    private Long childrenCount;
    private List<ChildExitResponseDto> childExitResponseDtoList;

    @Builder
    private ChildrenExitResponseDto(Long childrenCount, List<ChildExitResponseDto> childExitResponseDtoList){
        this.childrenCount = childrenCount;
        this.childExitResponseDtoList = childExitResponseDtoList;
    }

    public static ChildrenExitResponseDto of(Long childrenCount, List<ChildExitResponseDto> childExitResponseDtoList){
        return ChildrenExitResponseDto.builder()
                .childrenCount(childrenCount)
                .childExitResponseDtoList(childExitResponseDtoList)
                .build();
    }
}
