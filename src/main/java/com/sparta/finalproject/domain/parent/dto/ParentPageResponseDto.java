package com.sparta.finalproject.domain.parent.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.parent.entity.Parent;
import com.sparta.finalproject.global.enumType.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class ParentPageResponseDto {
    // 학부모 프로필

    private String kakaoEmail;

    private String name;

    private String phoneNumber;

    private String emergencyPhoneNumber;

    private String email;

    private String homePhone;

    private String parentProfileImageUrl;

    // 자녀 프로필
    private String childName;

    private Gender childGender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate childBirth;

    private String significant;

    private String childProfileImageUrl;

    private String className;

    private List<String> absentInfoList;

    @Builder
    private ParentPageResponseDto(Parent parent, Child child, List<String> absentInfoList) {
        this.kakaoEmail = parent.getKakaoEmail();
        this.name = parent.getName();
        this.phoneNumber = parent.getPhoneNumber();
        this.emergencyPhoneNumber = parent.getEmergencyPhoneNumber();
        this.email = parent.getEmail();
        this.homePhone = parent.getHomePhone();
        this.parentProfileImageUrl = parent.getProfileImageUrl();
        this.childName = child.getName();
        this.childGender = child.getGender();
        this.childBirth = child.getBirth();
        this.significant = child.getSignificant();
        this.childProfileImageUrl = child.getProfileImageUrl();
        this.className = child.getClassroom().getName();
        this.absentInfoList = absentInfoList;
    }

    public static ParentPageResponseDto from(Parent parent, Child child, List<String> absentInfoList) {
        return ParentPageResponseDto.builder()
                .parent(parent)
                .child(child)
                .absentInfoList(absentInfoList)
                .build();
    }

}
