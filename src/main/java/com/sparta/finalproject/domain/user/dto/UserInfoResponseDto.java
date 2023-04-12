package com.sparta.finalproject.domain.user.dto;

import com.sparta.finalproject.domain.kindergarten.dto.KindergartenResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private KindergartenResponseDto kindergarten;
    private Object userProfile;

    @Builder
    public UserInfoResponseDto(KindergartenResponseDto kindergarten, Object userProfile){
        this.kindergarten = kindergarten;
        this.userProfile = userProfile;
    }

    public static UserInfoResponseDto of(KindergartenResponseDto kindergarten, Object userProfile){
        return UserInfoResponseDto.builder()
                .kindergarten(kindergarten)
                .userProfile(userProfile)
                .build();
    }
}
