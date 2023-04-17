package com.sparta.finalproject.domain.attendance.dto;

import com.sparta.finalproject.domain.classroom.dto.ClassroomInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class DateResponseDto {

    private List<DateAttendanceResponseDto> content = new ArrayList<>();

    private List<ClassroomInfoDto> everyClass = new ArrayList<>();

    @Builder
    private DateResponseDto(List<DateAttendanceResponseDto> dateAttendanceResponseDtoList, List<ClassroomInfoDto> classroomInfoDtoList) {
        this.content = dateAttendanceResponseDtoList;
        this.everyClass = classroomInfoDtoList;

    }

    public static DateResponseDto of(List<DateAttendanceResponseDto> dateAttendanceResponseDtoList, List<ClassroomInfoDto> classroomInfoDtoList) {
        return DateResponseDto.builder()
                .dateAttendanceResponseDtoList(dateAttendanceResponseDtoList)
                .classroomInfoDtoList(classroomInfoDtoList)
                .build();
    }
}
