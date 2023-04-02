package com.sparta.finalproject.domain.gallery.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GalleryPageResponseDto {
    private Long imagePostCount;
    private List<ImagePostResponseDto> imagePostResponseDtoList;

    @Builder
    private GalleryPageResponseDto(Long imagePostCount, List<ImagePostResponseDto> imagePostResponseDtoList){
        this.imagePostCount = imagePostCount;
        this.imagePostResponseDtoList = imagePostResponseDtoList;
    }

    public static GalleryPageResponseDto of(Long imagePostCount, List<ImagePostResponseDto> imagePostResponseDtoList){
        return GalleryPageResponseDto.builder()
                .imagePostCount(imagePostCount)
                .imagePostResponseDtoList(imagePostResponseDtoList)
                .build();
    }
}
