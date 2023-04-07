package com.sparta.finalproject.domain.attendance.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.global.enumType.AttendanceStatus;
import com.sparta.finalproject.global.enumType.Day;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MonthAttendanceResponseDto {

    private Long id;

    private String name;

    private int attendanceCount;

    private int absentCount;

    private List<DayAttendanceResponseDto> monthAttendanceList = new ArrayList<>();

    @Builder
    private MonthAttendanceResponseDto(Child child, List<DayAttendanceResponseDto> dayAttendanceResponseDtoList,
                                       int attendanceCount, int absentCount) {
        this.id = child.getId();
        this.name = child.getName();
        this.attendanceCount = attendanceCount;
        this.absentCount = absentCount;
        this.monthAttendanceList = dayAttendanceResponseDtoList;

    }

    public static MonthAttendanceResponseDto of(Child child, List<DayAttendanceResponseDto> dayAttendanceResponseDtoList,
                                                int attendanceCount, int absentCount) {
        return MonthAttendanceResponseDto.builder()
                .child(child)
                .dayAttendanceResponseDtoList(dayAttendanceResponseDtoList)
                .attendanceCount(attendanceCount)
                .absentCount(absentCount)
                .build();
    }

}
