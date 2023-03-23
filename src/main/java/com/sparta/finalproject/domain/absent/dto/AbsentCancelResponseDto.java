package com.sparta.finalproject.domain.absent.dto;

import com.sparta.finalproject.domain.absent.entity.AbsentInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AbsentCancelResponseDto {
    List<String> absentList = new ArrayList<>();

    @Builder
    private AbsentCancelResponseDto(List<String> absentList) {
        this.absentList = absentList;
    }

    public static AbsentCancelResponseDto from(List<String> absentList) {
        return AbsentCancelResponseDto.builder()
                .absentList(absentList)
                .build();
    }
}
