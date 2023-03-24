package com.sparta.finalproject.domain.absent.dto;

import com.sparta.finalproject.domain.absent.entity.AbsentInfo;
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
    private AbsentAddResponseDto(AbsentInfo absentInfo) {
        startDate = absentInfo.getStartDate();
        endDate = absentInfo.getEndDate();
        reason = absentInfo.getReason();
    }

    public static AbsentAddResponseDto from(AbsentInfo absentInfo) {
        return AbsentAddResponseDto.builder()
                .absentInfo(absentInfo)
                .build();
    }
}
