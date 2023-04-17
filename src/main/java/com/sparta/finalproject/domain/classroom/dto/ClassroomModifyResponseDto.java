package com.sparta.finalproject.domain.classroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ClassroomModifyResponseDto {

    private Long id;

    private String name;

    @Builder
    private ClassroomModifyResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public static ClassroomModifyResponseDto of(Long id, String name) {
        return ClassroomModifyResponseDto.builder()
                .id(id)
                .name(name)
                .build();
    }
}
