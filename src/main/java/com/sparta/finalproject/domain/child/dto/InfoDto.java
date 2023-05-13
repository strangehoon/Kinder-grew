package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.user.dto.ParentListResponseDto;
import com.sparta.finalproject.domain.user.dto.ParentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class InfoDto {

    private int total;

    private int entered;

    private int notEntered;

    private int exited;

    private int absented;
    @Builder
    private InfoDto(int total, int entered, int notEntered, int exited, int absented) {
        this.total = total;
        this.entered = entered;
        this.notEntered = notEntered;
        this.exited = exited;
        this.absented = absented;
    }

    public static InfoDto of(int total, int entered, int notEntered, int exited, int absented) {
        return InfoDto.builder()
                .total(total)
                .entered(entered)
                .notEntered(notEntered)
                .exited(exited)
                .absented(absented)
                .build();
    }

}
