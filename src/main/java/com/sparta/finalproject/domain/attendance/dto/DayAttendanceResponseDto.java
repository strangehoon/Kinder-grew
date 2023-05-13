package com.sparta.finalproject.domain.attendance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.global.enumType.AttendanceStatus;
import com.sparta.finalproject.global.enumType.Day;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class DayAttendanceResponseDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Day day;
    private AttendanceStatus status;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime enterTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime exitTime;

    @Builder
    private DayAttendanceResponseDto(Attendance attendance, Day day) {
        this.date = attendance.getDate();
        this.day = day;
        this.status = attendance.getStatus();
        this.enterTime = attendance.getEnterTime();
        this.exitTime = attendance.getExitTime();
    }

    public static DayAttendanceResponseDto of(Attendance attendance, Day day) {
        return DayAttendanceResponseDto.builder()
                .day(day)
                .attendance(attendance)
                .build();
    }
}
