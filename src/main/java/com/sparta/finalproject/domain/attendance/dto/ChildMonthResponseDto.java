package com.sparta.finalproject.domain.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ChildMonthResponseDto {

    private InfoDto info;

    private List<AbsentInfoDto> absent;

    private List<ContentDto> content;

    @Builder
    private ChildMonthResponseDto(InfoDto infoDto, List<AbsentInfoDto> absentInfoDtoList, List<ContentDto> contentDtoList) {
        this.info = infoDto;
        this.absent = absentInfoDtoList;
        this.content = contentDtoList;
    }

    public static ChildMonthResponseDto of(InfoDto infoDto, List<AbsentInfoDto> absentInfoDtoList, List<ContentDto> contentDtoList) {
        return ChildMonthResponseDto.builder()
                .infoDto(infoDto)
                .absentInfoDtoList(absentInfoDtoList)
                .contentDtoList(contentDtoList)
                .build();
    }
}
