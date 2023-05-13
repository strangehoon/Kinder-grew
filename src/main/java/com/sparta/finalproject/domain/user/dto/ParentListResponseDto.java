package com.sparta.finalproject.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ParentListResponseDto {
    private List<ParentResponseDto> parentResponseDtoList;

    @Builder
    private ParentListResponseDto(List<ParentResponseDto> parentResponseDtoList) {
        this.parentResponseDtoList = parentResponseDtoList;
    }

    public static ParentListResponseDto from(List<ParentResponseDto> parentResponseDtoList) {
        return ParentListResponseDto.builder()
                .parentResponseDtoList(parentResponseDtoList)
                .build();
    }
}
