package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ChildEnterResponseDto {

    private String name;
    private String profileImageUrl;
    private boolean currentStatus;
    private String enterTime;
    private String exitTime;

    @Builder
    private ChildEnterResponseDto(Child child, Attendance attendance, String enterTime, String exitTime){
        this.name = child.getName();
        this.currentStatus = attendance.isEntered();
        this.profileImageUrl = child.getProfileImageUrl();
        this.enterTime = enterTime;
        this.exitTime = exitTime;
    }

    public static ChildEnterResponseDto of(Child child, Attendance attendance){
        return ChildEnterResponseDto.builder()
                .child(child)
                .attendance(attendance)
                .build();
    }

    public static ChildEnterResponseDto of(Child child, Attendance attendance, String enterTime){
        return ChildEnterResponseDto.builder()
                .child(child)
                .attendance(attendance)
                .enterTime(enterTime)
                .exitTime(null)
                .build();
    }

    public static ChildEnterResponseDto of(Child child, Attendance attendance, String enterTime, String exitTime){
        return ChildEnterResponseDto.builder()
                .child(child)
                .attendance(attendance)
                .enterTime(enterTime)
                .exitTime(exitTime)
                .build();
    }

}
