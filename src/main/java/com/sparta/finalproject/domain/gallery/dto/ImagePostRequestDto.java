package com.sparta.finalproject.domain.gallery.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ImagePostRequestDto {

    private String title;
    private List<MultipartFile> imageList;

}
