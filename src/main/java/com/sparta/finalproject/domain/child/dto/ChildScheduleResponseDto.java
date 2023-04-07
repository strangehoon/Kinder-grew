package com.sparta.finalproject.domain.child.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.finalproject.global.enumType.AttendanceStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
public class ChildScheduleResponseDto {
    private Long id;

    private String name;

    private String profileImageUrl;

    private LocalTime enterTime;

    private LocalTime exitTime;

    private AttendanceStatus state;


    @QueryProjection
    public ChildScheduleResponseDto(Long id, String name, String profileImageUrl, LocalTime enterTime, LocalTime exitTime,
                                    AttendanceStatus state){
        this.id = id;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.state = null;
    }

    public void update(AttendanceStatus state){
        this.state = state;
    }

}
