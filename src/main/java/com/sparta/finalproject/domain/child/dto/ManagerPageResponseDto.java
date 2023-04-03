package com.sparta.finalproject.domain.child.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManagerPageResponseDto {

    private Long totalNumber;
    private Long enterNumber;
    private Long notEnterNumber;
    private Long exitNumber;
    private ChildrenEnterResponseDto childrenEnterResponseDto;

    @Builder
    private ManagerPageResponseDto(Long totalNumber, Long enterNumber, Long notEnterNumber, Long exitNumber, ChildrenEnterResponseDto childrenEnterResponseDto){
        this.totalNumber =totalNumber;
        this.enterNumber = enterNumber;
        this.notEnterNumber = notEnterNumber;
        this.exitNumber = exitNumber;
        this.childrenEnterResponseDto = childrenEnterResponseDto;
    }

    public static ManagerPageResponseDto of(Long totalNumber, Long enterNumber, Long notEnterNumber, Long exitNumber, ChildrenEnterResponseDto childrenEnterResponseDto){
        return ManagerPageResponseDto.builder()
                .totalNumber(totalNumber)
                .enterNumber(enterNumber)
                .notEnterNumber(notEnterNumber)
                .exitNumber(exitNumber)
                .childrenEnterResponseDto(childrenEnterResponseDto)
                .build();
    }
}
