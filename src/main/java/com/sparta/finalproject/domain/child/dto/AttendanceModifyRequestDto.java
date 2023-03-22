package com.sparta.finalproject.domain.child.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AttendanceModifyRequestDto {

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime dailyEnterTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime dailyExitTime;
}
