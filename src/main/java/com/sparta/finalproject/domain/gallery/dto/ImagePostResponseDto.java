package com.sparta.finalproject.domain.gallery.dto;

import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ImagePostResponseDto {

    private final Long imagePostId;
    private final String title;
    private final String thumbnailImage;
    private final LocalDate createdAt;

    @Builder
    private ImagePostResponseDto(ImagePost imagePost, String thumbnailImage){
        this.imagePostId = imagePost.getId();
        this.title = imagePost.getTitle();
        this.createdAt = imagePost.getCreatedAt();
        this.thumbnailImage = thumbnailImage;
    }

    public static ImagePostResponseDto of(ImagePost imagePost, String thumbnailImage){
        return ImagePostResponseDto.builder()
                .imagePost(imagePost)
                .thumbnailImage(thumbnailImage)
                .build();
    }

}
