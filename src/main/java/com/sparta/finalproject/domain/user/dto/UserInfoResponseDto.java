package com.sparta.finalproject.domain.user.dto;

import com.sparta.finalproject.domain.child.dto.SidebarChildrenInfo;
import com.sparta.finalproject.domain.kindergarten.dto.KindergartenResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserInfoResponseDto {
    private KindergartenResponseDto kindergarten;
    private Object userProfile;
    private List<SidebarChildrenInfo> childList;

    @Builder
    public UserInfoResponseDto(KindergartenResponseDto kindergarten, Object userProfile, List<SidebarChildrenInfo> childList){
        this.kindergarten = kindergarten;
        this.userProfile = userProfile;
        this.childList = childList;
    }

    public static UserInfoResponseDto of(KindergartenResponseDto kindergarten, Object userProfile){
        return UserInfoResponseDto.builder()
                .kindergarten(kindergarten)
                .userProfile(userProfile)
                .build();
    }

    public static UserInfoResponseDto of(KindergartenResponseDto kindergarten, Object userProfile, List<SidebarChildrenInfo> childList){
        return UserInfoResponseDto.builder()
                .kindergarten(kindergarten)
                .userProfile(userProfile)
                .childList(childList)
                .build();
    }
}
