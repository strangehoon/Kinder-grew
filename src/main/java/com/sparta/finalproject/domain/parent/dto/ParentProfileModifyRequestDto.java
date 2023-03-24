package com.sparta.finalproject.domain.parent.dto;

import com.sparta.finalproject.global.enumType.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
@Getter
@NoArgsConstructor
@Setter
public class ParentProfileModifyRequestDto {

    // 학부모 프로필

    private String phoneNumber;

    private String emergencyPhoneNumber;

    private String email;

    private String homePhone;

    private MultipartFile parentImage;

    private String relationship;

    // 자녀 프로필
    private String childName;

    private Gender childGender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate childBirth;

    private String significant;

    private MultipartFile childImage;
}
