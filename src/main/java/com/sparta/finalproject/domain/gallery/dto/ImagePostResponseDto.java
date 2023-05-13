package com.sparta.finalproject.domain.gallery.dto;

import com.sparta.finalproject.domain.gallery.entity.ImagePost;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ImagePostResponseDto {

    private final Long imagePostId;
    private final String title;
    private final List<String> imageUrlList;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
