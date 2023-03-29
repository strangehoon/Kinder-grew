package com.sparta.finalproject.domain.child.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ManagerPageResponseDto {

    private Long totalNumber;
    private Long enterNumber;
    private Long notEnterNumber;
    private Long exitNumber;
    private List<ChildEnterResponseDto> childEnterResponseDtoList = new ArrayList<>();

    @Builder
    private ManagerPageResponseDto(Long totalNumber, Long enterNumber, Long notEnterNumber, Long exitNumber, List<ChildEnterResponseDto> childEnterResponseDtoList){
        this.totalNumber =totalNumber;
        this.enterNumber = enterNumber;
        this.notEnterNumber = notEnterNumber;
        this.exitNumber = exitNumber;
        this.childEnterResponseDtoList = childEnterResponseDtoList;
    }

    public static ManagerPageResponseDto of(Long totalNumber, Long enterNumber, Long notEnterNumber, Long exitNumber, List<ChildEnterResponseDto> childEnterResponseDtoList){
        return ManagerPageResponseDto.builder()
                .totalNumber(totalNumber)
                .enterNumber(enterNumber)
                .notEnterNumber(notEnterNumber)
                .exitNumber(exitNumber)
                .childEnterResponseDtoList(childEnterResponseDtoList)
                .build();
    }
}
