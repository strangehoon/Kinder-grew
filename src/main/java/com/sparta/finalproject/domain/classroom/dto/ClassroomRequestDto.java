package com.sparta.finalproject.domain.classroom.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class ClassroomRequestDto {
    @NotNull(message = "반 이름을 입력해 주세요.")
    private String name;
}
