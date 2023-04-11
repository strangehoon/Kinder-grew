package com.sparta.finalproject.domain.user.dto;

import com.sparta.finalproject.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String name;
    private String profileImageUrl;

    @Builder
    private MemberResponseDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.profileImageUrl = user.getProfileImageUrl();
    }

    public static MemberResponseDto of(User user){
        return MemberResponseDto.builder()
                .user(user)
                .build();
    }
}
