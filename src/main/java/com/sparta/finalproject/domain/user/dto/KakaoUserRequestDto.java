package com.sparta.finalproject.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserRequestDto {

    private Long kakaoId;

    private String name;

    @Builder
    private KakaoUserRequestDto(Long kakaoId, String name) {
        this.kakaoId = kakaoId;
        this.name = name;
    }
}
