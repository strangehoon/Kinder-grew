package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttendanceModifyResponseDto {

    private String dailyEnterTime;

    private String dailyExitTime;



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
