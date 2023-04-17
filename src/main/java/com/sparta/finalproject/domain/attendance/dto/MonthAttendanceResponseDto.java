package com.sparta.finalproject.domain.attendance.dto;

import com.sparta.finalproject.domain.classroom.dto.ClassroomInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MonthAttendanceResponseDto {

    private List<ClassroomInfoDto> everyClass = new ArrayList<>();

    private List<MonthAttendanceDto> content = new ArrayList<>();

    @Builder
    private MonthAttendanceResponseDto(List<ClassroomInfoDto> everyClass, List<MonthAttendanceDto> content) {
        this.everyClass = everyClass;
        this.content = content;

    }

    public static MonthAttendanceResponseDto of(List<ClassroomInfoDto> everyClass, List<MonthAttendanceDto> content) {
        return MonthAttendanceResponseDto.builder()
                .everyClass(everyClass)
                .content(content)
                .build();
    }
}
