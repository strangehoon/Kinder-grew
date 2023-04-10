package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChildAttendanceTimeResponseDto {

    private String dailyEnterTime;

    private String dailyExitTime;

    @Builder
    private ChildAttendanceTimeResponseDto(Child child){
        dailyEnterTime = child.getDailyEnterTime();
        dailyExitTime = child.getDailyExitTime();
    }

    public static ChildAttendanceTimeResponseDto from(Child child) {
        return ChildAttendanceTimeResponseDto.builder()
                .child(child)
                .build();
    }
}
