package com.sparta.finalproject.domain.attendance.dto;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.global.enumType.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ContentDto {

    private int date;

    private LocalTime enterTime;

    private LocalTime exitTime;

    private AttendanceStatus status;

    @Builder
    private ContentDto(Attendance attendance) {
        this.date = attendance.getDate().getDayOfMonth();
        this.enterTime = attendance.getEnterTime();
        this.exitTime = attendance.getExitTime();
        this.status = attendance.getStatus();
    }

    public static ContentDto from(Attendance attendance) {
        return ContentDto.builder()
                .attendance(attendance)
                .build();
    }
}
