package com.sparta.finalproject.domain.classroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClassroomInfoDto {

    private Long id;

    private String name;

    @Builder
    private ClassroomInfoDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public static ClassroomInfoDto of(Long id, String name) {
        return ClassroomInfoDto.builder()
                .id(id)
                .name(name)
                .build();
    }
}
