package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChildExitResponseDto {

    private String name;
    private String profileImageUrl;
    private String currentStatus;
    private String enterTime;
    private String exitTime;

    @Builder
    private ChildExitResponseDto(Child child, String currentStatus, String enterTime, String exitTime){
        this.name = child.getName();
        this.currentStatus = currentStatus;
        this.profileImageUrl = child.getProfileImageUrl();
        this.enterTime = enterTime;
        this.exitTime = exitTime;
    }

    public static ChildExitResponseDto of(Child child, String currentStatus){
        return ChildExitResponseDto.builder()
                .child(child)
                .currentStatus(currentStatus)
                .build();
    }

    public static ChildExitResponseDto of(Child child, String currentStatus, String enterTime){
        return ChildExitResponseDto.builder()
                .child(child)
                .currentStatus(currentStatus)
                .enterTime(enterTime)
                .exitTime(null)
                .build();
    }

    public static ChildExitResponseDto of(Child child, String currentStatus, String enterTime, String exitTime){
        return ChildExitResponseDto.builder()
                .child(child)
                .currentStatus(currentStatus)
                .enterTime(enterTime)
                .exitTime(exitTime)
                .build();
    }

}
