package com.sparta.finalproject.domain.user.dto;

import com.sparta.finalproject.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParentResponseDto {
    private Long parentId;
    private String name;
    private String phoneNumber;
    private String email;
    private String profileImageUrl;


    @Builder
    public ParentResponseDto(User user) {
        this.parentId = user.getId();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.profileImageUrl = user.getProfileImageUrl();
        this.email = user.getEmail();
    }

    public static ParentResponseDto from(User user) {
        return ParentResponseDto.builder()
                .user(user)
                .build();
    }
}
