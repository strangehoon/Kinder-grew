package com.sparta.finalproject.domain.attendance.dto;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ExitResponseDto {

    private Long childId;
    private String name;
    private boolean isExited;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime dailyEnterTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime dailyExitTime;

    @Builder
    private ExitResponseDto (Attendance attendance) {
        this.childId = attendance.getChild().getId();
        this.name = attendance.getChild().getName();
        this.dailyEnterTime = attendance.getChild().getDailyEnterTime();
        this.dailyExitTime = attendance.getChild().getDailyExitTime();
        this.isExited = attendance.isEntered();
    }

    public static ExitResponseDto of (Attendance attendance){
        return ExitResponseDto.builder()
                .attendance(attendance)
                .build();
    }
}
