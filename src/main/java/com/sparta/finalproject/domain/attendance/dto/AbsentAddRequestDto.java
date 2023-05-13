package com.sparta.finalproject.domain.attendance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AbsentAddRequestDto {

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;
}
