package com.sparta.finalproject.domain.gallery.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ImagePostResponseDto {

    private final Long imagePostId;
    private final String title;
    private final List<String> imageUrlList;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate createdAt;

    @Builder
    private ImagePostResponseDto(ImagePost imagePost, List<String> imageUrlList){
        this.imagePostId = imagePost.getId();
        this.title = imagePost.getTitle();
        this.createdAt = imagePost.getCreatedAt();
        this.imageUrlList = imageUrlList;
    }

    public static ImagePostResponseDto of(ImagePost imagePost, List<String> imageUrlList){
        return ImagePostResponseDto.builder()
                .imagePost(imagePost)
                .imageUrlList(imageUrlList)
                .build();
    }

}
