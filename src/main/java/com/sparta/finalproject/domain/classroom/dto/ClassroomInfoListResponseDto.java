package com.sparta.finalproject.domain.classroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ClassroomInfoListResponseDto {

    private List<ClassroomInfoDto> classList;

    @Builder
    private ClassroomInfoListResponseDto(List<ClassroomInfoDto> classList) {
        this.classList = classList;
    }
    public static ClassroomInfoListResponseDto from(List<ClassroomInfoDto> classList) {
        return ClassroomInfoListResponseDto.builder()
                .classList(classList)
                .build();
    }
}
