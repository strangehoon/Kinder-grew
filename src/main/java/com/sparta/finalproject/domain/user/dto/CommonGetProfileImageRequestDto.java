package com.sparta.finalproject.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public abstract class CommonGetProfileImageRequestDto {

    private String isCancelled;

    private MultipartFile profileImage;
}
