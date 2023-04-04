package com.sparta.finalproject.domain.attendance.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AbsentAddResponseDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String reason;

    @Builder
    private AbsentAddResponseDto(LocalDate startDate, LocalDate endDate, String reason) {
        startDate = startDate;
        endDate = endDate;
        reason = reason;
    }

    public static AbsentAddResponseDto of(LocalDate startDate, LocalDate endDate, String reason) {
        return AbsentAddResponseDto.builder()
                .startDate(startDate)
                .endDate(endDate)
                .reason(reason)
                .build();
    }
}
