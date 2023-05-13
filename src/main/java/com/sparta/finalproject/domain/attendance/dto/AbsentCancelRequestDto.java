package com.sparta.finalproject.domain.attendance.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AbsentCancelRequestDto {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

}
