package com.sparta.finalproject.domain.attendance.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MonthAttendanceDto {

    private Long id;

    private String name;

    private int attendanceCount;

    private int absentCount;

    private List<DayAttendanceResponseDto> monthAttendanceList = new ArrayList<>();

    @Builder
    private MonthAttendanceDto(Child child, List<DayAttendanceResponseDto> dayAttendanceResponseDtoList,
                               int attendanceCount, int absentCount) {
        this.id = child.getId();
        this.name = child.getName();
        this.attendanceCount = attendanceCount;
        this.absentCount = absentCount;
        this.monthAttendanceList = dayAttendanceResponseDtoList;

    }

    public static MonthAttendanceDto of(Child child, List<DayAttendanceResponseDto> dayAttendanceResponseDtoList,
                                        int attendanceCount, int absentCount) {
        return MonthAttendanceDto.builder()
                .child(child)
                .dayAttendanceResponseDtoList(dayAttendanceResponseDtoList)
                .attendanceCount(attendanceCount)
                .absentCount(absentCount)
                .build();
    }

}
