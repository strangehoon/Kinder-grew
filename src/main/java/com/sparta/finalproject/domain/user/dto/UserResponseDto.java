package com.sparta.finalproject.domain.user.dto;

import com.sparta.finalproject.domain.kindergarten.entity.Kindergarten;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private String name;

    private String profileImageUrl;

    private String kindergartenName;

    private String logoImageUrl;

    @Builder
    private UserResponseDto(String name, String profileImageUrl, Kindergarten kindergarten) {

        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.kindergartenName = kindergarten.getKindergartenName();
        this.logoImageUrl = kindergarten.getLogoImageUrl();
    }

    public static UserResponseDto of(String name, String profileImageUrl, Kindergarten kindergarten) {
        return UserResponseDto.builder()
                .name(name)
                .profileImageUrl(profileImageUrl)
                .kindergarten(kindergarten)
                .build();
    }

    public static UserResponseDto of(String name, String profileImageUrl) {
        return UserResponseDto.builder()
                .name(name)
                .profileImageUrl(profileImageUrl)
                .build();
    }
}
