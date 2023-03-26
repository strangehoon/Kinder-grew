package com.sparta.finalproject.domain.classroom.dto;

import com.sparta.finalproject.domain.classroom.entity.Teacher;
import com.sparta.finalproject.global.enumType.Gender;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class TeacherResponseDto {
    private final String name;
    private final Gender gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birth;
    private final String phoneNumber;
    private final String email;
    private final String resolution;
    private final String profileImageUrl;

    @Builder
    private TeacherResponseDto(Teacher teacher){
        this.name = teacher.getName();
        this.gender = teacher.getGender();
        this.birth = teacher.getBirth();
        this.email = teacher.getEmail();
        this.profileImageUrl = teacher.getProfileImageURl();
        this.phoneNumber = teacher.getPhoneNumber();
        this.resolution = teacher.getResolution();
    }

    public static TeacherResponseDto of(Teacher teacher){
        return TeacherResponseDto.builder()
                .teacher(teacher)
                .build();
    }

}
