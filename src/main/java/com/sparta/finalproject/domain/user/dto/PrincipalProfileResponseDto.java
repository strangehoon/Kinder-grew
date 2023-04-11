package com.sparta.finalproject.domain.user.dto;

import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PrincipalProfileResponseDto {
    private String name;

    private LocalDate birthday;

    private String phoneNumber;

    private String profileImageUrl;

    private UserRoleEnum role;

    private String email;

    @Builder
    private PrincipalProfileResponseDto(User user) {

        this.name = user.getName();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        this.phoneNumber = user.getPhoneNumber();
        this.profileImageUrl = user.getProfileImageUrl();
        this.role = user.getRole();
    }

    public static PrincipalProfileResponseDto from(User user) {
        return PrincipalProfileResponseDto.builder()
                .user(user)
                .build();
    }
}
