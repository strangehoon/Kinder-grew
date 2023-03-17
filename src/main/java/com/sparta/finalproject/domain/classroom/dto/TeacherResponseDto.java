package com.sparta.finalproject.domain.classroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.finalproject.domain.classroom.entity.Teacher;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TeacherResponseDto {
    private final String name;
    private final int age;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate birth;
    private final String phoneNumber;
    private final String license;
    private final String email;
    private final String resolution;
    private final String imageUrl;

    @Builder
    private TeacherResponseDto(Teacher teacher){
        this.age = teacher.getAge();
        this.name = teacher.getName();
        this.birth = teacher.getBirth();
        this.email = teacher.getEmail();
        this.license = teacher.getLicense();
        this.imageUrl = teacher.getImageUrl();
        this.phoneNumber = teacher.getPhoneNumber();
        this.resolution = teacher.getResolution();
    }

    public static TeacherResponseDto of(Teacher teacher){
        return TeacherResponseDto.builder()
                .teacher(teacher)
                .build();
    }

}
