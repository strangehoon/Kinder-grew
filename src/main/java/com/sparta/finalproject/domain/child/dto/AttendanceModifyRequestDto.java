package com.sparta.finalproject.domain.child.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttendanceModifyRequestDto {

    private String dailyEnterTime;

    private String dailyExitTime;
}
