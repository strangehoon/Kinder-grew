package com.sparta.finalproject.domain.child.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.global.enumType.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
public class ChildResponseDto {
    private Long childId;
    private String name;
    private int age;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birth;
    private Gender gender;
    private String significant;
    private String profileImageUrl;

    private String dailyEnterTime;
    private String dailyExitTime;

    @Builder
    private ChildResponseDto(Child child) {
        this.name = child.getName();
        this.age = child.getAge();
        this.birth = child.getBirth();
        this.gender = child.getGender();
        this.significant = child.getSignificant();
        this.profileImageUrl = child.getProfileImageUrl();
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