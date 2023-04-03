package com.sparta.finalproject.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserRequestDto {

    private Long kakaoId;

    private String name;

    private String profileImageUrl;

    @Builder
    public KakaoUserRequestDto(Long kakaoId, String name, String profileImageUrl) {
        this.kakaoId = kakaoId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

}
