package com.sparta.finalproject.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserResponseDto {

    private String name;
    private String profileImageUrl;

    @Builder
    private KakaoUserResponseDto(String name, String profileImageUrl) {

        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public static KakaoUserResponseDto of(String name, String profileImageUrl) {
        return KakaoUserResponseDto.builder()
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
