package com.sparta.finalproject.domain.classroom.dto;

import com.sparta.finalproject.domain.attendance.dto.AbsentInfoDto;
import com.sparta.finalproject.domain.attendance.dto.ChildMonthResponseDto;
import com.sparta.finalproject.domain.attendance.dto.ContentDto;
import com.sparta.finalproject.domain.attendance.dto.InfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ClassroomAddResponseDto {

    private Long id;

    private String name;

    @Builder
    private ClassroomAddResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public static ClassroomAddResponseDto of(Long id, String name) {
        return ClassroomAddResponseDto.builder()
                .id(id)
                .name(name)
                .build();
    }
}
