package com.sparta.finalproject.domain.classroom.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TeacherRequestDto {
    private String name;
    private int age;
    private LocalDate birth;
    private String phoneNumber;
    private String license;
    private String email;
    private String resolution;
    private String image;
}
