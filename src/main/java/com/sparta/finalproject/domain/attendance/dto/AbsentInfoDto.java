package com.sparta.finalproject.domain.attendance.dto;

import com.sparta.finalproject.domain.attendance.entity.AbsentInfo;
import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.global.enumType.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class AbsentInfoDto {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    @Builder
    private AbsentInfoDto(AbsentInfo absentInfo) {
        this.id = absentInfo.getId();
        this.startDate = absentInfo.getStartDate();
        this.endDate = absentInfo.getEndDate();
        this.reason = absentInfo.getReason();
    }

    public static AbsentInfoDto from(AbsentInfo absentInfo) {
        return AbsentInfoDto.builder()
                .absentInfo(absentInfo)
                .build();
    }
}
