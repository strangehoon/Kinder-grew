package com.sparta.finalproject.domain.attendance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.finalproject.global.enumType.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DateAttendanceResponseDto {

    private Long id;

    private String name;

    private AttendanceStatus status;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime enterTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime exitTime;

    private String absentReason;
}
