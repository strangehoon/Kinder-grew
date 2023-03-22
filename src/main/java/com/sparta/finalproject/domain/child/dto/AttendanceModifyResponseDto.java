package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AttendanceModifyResponseDto {

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime dailyEnterTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime dailyExitTime;



    @Builder
    private AttendanceModifyResponseDto(Child child){
        dailyEnterTime = child.getDailyEnterTime();
        dailyExitTime = child.getDailyExitTime();
    }

    public static AttendanceModifyResponseDto from(Child child) {
        return AttendanceModifyResponseDto.builder()
                .child(child)
                .build();
    }

}
