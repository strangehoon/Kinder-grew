package com.sparta.finalproject.domain.user.entity;

import com.sparta.finalproject.domain.user.dto.KakaoUserRequestDto;
import com.sparta.finalproject.domain.user.dto.ParentSignupRequestDto;
import com.sparta.finalproject.domain.user.dto.TeacherSignupRequestDto;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long kakaoId;

    @Column(length = 15)
    private String name;

    @Column(unique = true, length = 20)
    private String phoneNumber;

    @Column
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column
    private String profileImageUrl;

    @Column(length = 20)
    private String relationship;

    @Column(unique = true, length = 30)
    private String emergencyPhoneNumber;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column
    private String resolution;

    @Builder
    public User(KakaoUserRequestDto requestDto, UserRoleEnum role, String profileImageUrl) {

        this.kakaoId = requestDto.getKakaoId();
        this.role = role;
        this.name = requestDto.getName();
        this.profileImageUrl = profileImageUrl;
        this.phoneNumber = null;
        this.relationship = null;
        this.emergencyPhoneNumber = null;
        this.birthday = null;
        this.resolution = null;
    }

    public void update(ParentSignupRequestDto requestDto, UserRoleEnum role, String profileImageUrl) {

        this.name = requestDto.getName();
        this.role = role;
        this.phoneNumber = requestDto.getPhoneNumber();
        this.profileImageUrl = profileImageUrl;
        this.relationship = requestDto.getRelationship();
        this.emergencyPhoneNumber= requestDto.getEmergencyPhoneNumber();
    }

    public void update(TeacherSignupRequestDto requestDto, UserRoleEnum role, String profileImageUrl) {

        this.name = requestDto.getName();
        this.role = role;
        this.phoneNumber = requestDto.getPhoneNumber();
        this.profileImageUrl = profileImageUrl;
        this.birthday = requestDto.getBirthday();
        this.resolution = requestDto.getResolution();
    }
}
