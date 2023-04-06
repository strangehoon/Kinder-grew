package com.sparta.finalproject.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private String name;
    private String profileImageUrl;

    @Builder
    private UserResponseDto(String name, String profileImageUrl) {

        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }

    public static UserResponseDto of(String name, String profileImageUrl) {
        return UserResponseDto.builder()
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
