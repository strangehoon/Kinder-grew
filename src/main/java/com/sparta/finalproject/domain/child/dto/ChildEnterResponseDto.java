package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChildEnterResponseDto {

    private String name;
    private String profileImageUrl;
    private String currentStatus;
    private String enterTime;
    private String exitTime;

    @Builder
    private ChildEnterResponseDto(Child child, String currentStatus , String enterTime, String exitTime){
        this.name = child.getName();
        this.currentStatus = currentStatus;
        this.profileImageUrl = child.getProfileImageUrl();
        this.enterTime = enterTime;
        this.exitTime = exitTime;
    }

    public static ChildEnterResponseDto of(Child child, String currentStatus){
        return ChildEnterResponseDto.builder()
                .child(child)
                .currentStatus(currentStatus)
                .build();
    }

    public static ChildEnterResponseDto of(Child child, String currentStatus, String enterTime){
        return ChildEnterResponseDto.builder()
                .child(child)
                .currentStatus(currentStatus)
                .enterTime(enterTime)
                .exitTime(null)
                .build();
    }

    public static ChildEnterResponseDto of(Child child, String currentStatus, String enterTime, String exitTime){
        return ChildEnterResponseDto.builder()
                .child(child)
                .currentStatus(currentStatus)
                .enterTime(enterTime)
                .exitTime(exitTime)
                .build();
    }

}
