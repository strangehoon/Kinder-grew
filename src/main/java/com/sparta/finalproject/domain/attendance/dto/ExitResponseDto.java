package com.sparta.finalproject.domain.attendance.dto;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExitResponseDto {

    private Long childId;
    private String name;
    private boolean isExited;
    private String dailyEnterTime;
    private String dailyExitTime;

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
