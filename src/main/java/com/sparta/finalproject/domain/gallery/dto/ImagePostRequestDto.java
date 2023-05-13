package com.sparta.finalproject.domain.gallery.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ImagePostRequestDto {

    @NotNull
    @Size(max = 15, message = "제목은 15글자를 넘을 수 없습니다.")
    private String title;
    private List<MultipartFile> imageList;

}
