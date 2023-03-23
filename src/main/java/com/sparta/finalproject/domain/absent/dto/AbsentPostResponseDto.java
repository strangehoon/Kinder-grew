package com.sparta.finalproject.domain.absent.dto;

import com.sparta.finalproject.domain.absent.entity.AbsentInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AbsentPostResponseDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String reason;


    @Builder
    private AbsentPostResponseDto(AbsentInfo absentInfo) {
        startDate = absentInfo.getStartDate();
        endDate = absentInfo.getEndDate();
        reason = absentInfo.getReason();
    }

    public static AbsentPostResponseDto from(AbsentInfo absentInfo) {
        return AbsentPostResponseDto.builder()
                .absentInfo(absentInfo)
                .build();
    }
}
