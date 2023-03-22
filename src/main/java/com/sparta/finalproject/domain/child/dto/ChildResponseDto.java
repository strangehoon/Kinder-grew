package com.sparta.finalproject.domain.child.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.finalproject.domain.child.entity.Child;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ChildResponseDto {

    private String name;
    private int age;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birth;
    private String gender;
    private String significant;
    private Long childId;
    private String Image;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate dailyEnterTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate dailyExitTime;

    @Builder
    private ChildResponseDto(Child child) {
        this.name = child.getName();
        this.age = child.getAge();
        this.birth = child.getBirth();
        this.gender = child.getGender();
        this.significant = child.getSignificant();
        this.Image = child.getImageUrl();
        this.dailyEnterTime = child.getDailyEnterTime();
        this.dailyExitTime = child.getDailyExitTime();
        this.childId = child.getId();
    }

    public static ChildResponseDto of(Child child) {
        return ChildResponseDto.builder()
                .child(child)
                .build();
    }
}