package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.global.enumType.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class ChildRequestDto {
    private String name;
    private int age;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private Gender gender;
    private String significant;
    private MultipartFile image;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime dailyEnterTime;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime dailyExitTime;
}
