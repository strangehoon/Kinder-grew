package com.sparta.finalproject.domain.classroom.dto;

import com.sparta.finalproject.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class ClassroomTeacherResponseDto {
    private Long id;
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String phoneNumber;
    private String email;
    private String resolution;
    private String profileImageUrl;

    @Builder
    public ClassroomTeacherResponseDto(User teacher){
        this.id = teacher.getId();
        this.name = teacher.getName();
        this.birth = teacher.getBirthday();
        this.phoneNumber = teacher.getPhoneNumber();
        this.email = teacher.getEmail();
        this.resolution = teacher.getResolution();
        this.profileImageUrl = teacher.getProfileImageUrl();
    }

}
