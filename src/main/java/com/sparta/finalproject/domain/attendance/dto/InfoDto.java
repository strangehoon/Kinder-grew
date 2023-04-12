package com.sparta.finalproject.domain.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InfoDto {

    private long attendanceCount;

    private long absentCount;
}
