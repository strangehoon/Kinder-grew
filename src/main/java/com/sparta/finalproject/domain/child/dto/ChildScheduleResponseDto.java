package com.sparta.finalproject.domain.child.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sparta.finalproject.global.enumType.Status;
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

    private Status state;


    @QueryProjection
    public ChildScheduleResponseDto(Long id, String name, String profileImageUrl, LocalTime enterTime, LocalTime exitTime,
                                    Status state){
        this.id = id;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.state = null;
    }

    public void update(Status state){
        this.state = state;
    }

}
