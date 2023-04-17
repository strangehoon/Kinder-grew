package com.sparta.finalproject.domain.classroom.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class ClassroomRequestDto {
    @NotNull(message = "반 이름을 입력해 주세요.")
    @Size(max = 20, message = "유치원 이름은 20글자를 넘을 수 없습니다.")
    private String name;
}
