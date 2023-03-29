package com.sparta.finalproject.domain.attendance.dto;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AbsentResponseDto {

    private Long childId;
    private String name;
    private boolean isAbsent;
    private String dailyEnterTime;
    private String dailyExitTime;


    @Builder
    private AbsentResponseDto (Attendance attendance) {
        this.childId = attendance.getChild().getId();
        this.name = attendance.getChild().getName();
        this.dailyEnterTime = attendance.getChild().getDailyEnterTime();
        this.dailyExitTime = attendance.getChild().getDailyExitTime();
        this.isAbsent = attendance.isEntered();
    }

    public static AbsentResponseDto of (Attendance attendance){
        return AbsentResponseDto.builder()
                .attendance(attendance)
                .build();
    }
}
