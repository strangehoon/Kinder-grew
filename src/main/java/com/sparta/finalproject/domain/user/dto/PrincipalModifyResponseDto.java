package com.sparta.finalproject.domain.user.dto;

import com.sparta.finalproject.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PrincipalModifyResponseDto {

    private String name;
    private String profileImageUrl;

    @Builder
    private PrincipalModifyResponseDto(User user){
        this.name = user.getName();
        this.profileImageUrl = user.getProfileImageUrl();
    }

    public static PrincipalModifyResponseDto from (User user){
        return PrincipalModifyResponseDto.builder()
                .user(user)
                .build();
    }

}
