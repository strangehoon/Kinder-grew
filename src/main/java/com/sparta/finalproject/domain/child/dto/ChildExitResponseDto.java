package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChildExitResponseDto {

    private String name;
    private String profileImageUrl;
    private boolean currentStatus;
    private String enterTime;
    private String exitTime;

    @Builder
    private ChildExitResponseDto(Child child, Attendance attendance, String enterTime, String exitTime){
        this.name = child.getName();
        this.currentStatus = attendance.isExited();
        this.profileImageUrl = child.getProfileImageUrl();
        this.enterTime = enterTime;
        this.exitTime = exitTime;
    }

    public static ChildExitResponseDto of(Child child, Attendance attendance){
        return ChildExitResponseDto.builder()
                .child(child)
                .attendance(attendance)
                .build();
    }

    public static ChildExitResponseDto of(Child child, Attendance attendance, String enterTime){
        return ChildExitResponseDto.builder()
                .child(child)
                .attendance(attendance)
                .enterTime(enterTime)
                .exitTime(null)
                .build();
    }

    public static ChildExitResponseDto of(Child child, Attendance attendance, String enterTime, String exitTime){
        return ChildExitResponseDto.builder()
                .child(child)
                .attendance(attendance)
                .enterTime(enterTime)
                .exitTime(exitTime)
                .build();
    }

}
