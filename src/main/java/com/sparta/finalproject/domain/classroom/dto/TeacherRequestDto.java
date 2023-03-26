package com.sparta.finalproject.domain.classroom.dto;

import com.sparta.finalproject.global.enumType.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class TeacherRequestDto {
    private String name;
    private Gender gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private String phoneNumber;
    private String email;
    private String resolution;
    private MultipartFile image;
}
