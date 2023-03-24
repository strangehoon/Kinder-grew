package com.sparta.finalproject.domain.classroom.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TeacherRequestDto {
    private String name;
    private String gender;
    private LocalDate birth;
    private String phoneNumber;
    private String email;
    private String resolution;
}
