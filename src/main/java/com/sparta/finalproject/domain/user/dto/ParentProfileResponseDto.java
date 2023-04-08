package com.sparta.finalproject.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParentProfileResponseDto {

    private String name;

    private String phoneNumber;

    private String profileImageUrl;

    private UserRoleEnum role;

    private String emergencyPhoneNumber;

    private String email;

    @Builder
    private ParentProfileResponseDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.profileImageUrl = user.getProfileImageUrl();
        this.emergencyPhoneNumber = user.getEmergencyPhoneNumber();
        this.role = user.getRole();
    }

    public static ParentProfileResponseDto of(User user) {
        return ParentProfileResponseDto.builder()
                .user(user)
                .build();
    }
}
