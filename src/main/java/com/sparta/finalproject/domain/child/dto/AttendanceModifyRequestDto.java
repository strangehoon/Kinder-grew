package com.sparta.finalproject.domain.child.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AttendanceModifyRequestDto {
    private String dailyEnterTime;

    private String dailyExitTime;
}
