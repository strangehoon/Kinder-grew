package com.sparta.finalproject.domain.user.dto;

import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private String name;

    private String profileImageUrl;
    private UserRoleEnum role;

    private String kindergartenName;

    private String logoImageUrl;

    @Builder
    private UserResponseDto(User user, String kindergartenName, String logoImageUrl) {

        this.name = user.getName();
        this.profileImageUrl = user.getProfileImageUrl();
        this.role = user.getRole();
        this.kindergartenName = kindergartenName;
        this.logoImageUrl = logoImageUrl;
    }

    public static UserResponseDto of(User user, String kindergartenName, String logoImageUrl) {
        return UserResponseDto.builder()
                .user(user)
                .kindergartenName(kindergartenName)
                .logoImageUrl(logoImageUrl)
                .build();
    }

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .user(user)
                .build();
    }
}
