package com.sparta.finalproject.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeacherProfileResponseDto {

    private String name;

    private LocalDate birthday;

    private String phoneNumber;

    private String profileImageUrl;

    private String resolution;

    private UserRoleEnum role;

    private String email;

    @Builder
    private TeacherProfileResponseDto(User user) {

        this.name = user.getName();
        this.email = user.getEmail();
        this.birthday = user.getBirthday();
        this.phoneNumber = user.getPhoneNumber();
        this.profileImageUrl = user.getProfileImageUrl();
        this.resolution = user.getResolution();
        this.role = user.getRole();
    }

    public static TeacherProfileResponseDto of(User user) {

        return TeacherProfileResponseDto.builder()
                .user(user)
                .build();
    }
}
