package com.sparta.finalproject.domain.attendance.dto;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EnterResponseDto {

    private Long childId;
    private String name;
    private boolean isEntered;
    private String dailyEnterTime;
    private String dailyExitTime;

    @Builder
    private EnterResponseDto (Attendance attendance) {
        this.childId = attendance.getChild().getId();
        this.name = attendance.getChild().getName();
        this.dailyEnterTime = attendance.getChild().getDailyEnterTime();
        this.dailyExitTime = attendance.getChild().getDailyExitTime();
        this.isEntered = attendance.isEntered();
    }

    public static EnterResponseDto of (Attendance attendance){
        return EnterResponseDto.builder()
                .attendance(attendance)
                .build();
    }
}
