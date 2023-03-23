package com.sparta.finalproject.domain.parent.dto;

import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.domain.parent.entity.Parent;
import com.sparta.finalproject.global.enumType.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@NoArgsConstructor
public class ParentProfileModifyResponseDto {
    // 학부모 프로필
    private String phoneNumber;

    private String emergencyPhoneNumber;

    private String email;

    private String homePhone;

    private String parentProfileImageUrl;

    // 자녀 프로필

    private String childName;

    private Gender childGender;

    private LocalDate childBirth;

    private String significant;
    private String childProfileImageUrl;

    @Builder
    private ParentProfileModifyResponseDto(Parent parent, Child child){
        phoneNumber = parent.getPhoneNumber();
        emergencyPhoneNumber = parent.getEmergencyPhoneNumber();
        email = parent.getEmail();
        homePhone = parent.getHomePhone();
        parentProfileImageUrl = parent.getProfileImageUrl();
        childName = child.getName();
        childGender = child.getGender();
        childBirth = child.getBirth();
        significant = child.getSignificant();
        childProfileImageUrl = child.getProfileImageUrl();
    }

    public static ParentProfileModifyResponseDto from(Parent parent, Child child) {
        return ParentProfileModifyResponseDto.builder()
                .parent(parent)
                .child(child)
                .build();
    }
}
