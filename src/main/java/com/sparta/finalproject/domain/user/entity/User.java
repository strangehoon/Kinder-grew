package com.sparta.finalproject.domain.user.entity;

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
    public User(Long kakaoId, UserRoleEnum role, String name, String phoneNumber,String profileImageUrl,
                String relationship, String emergencyPhoneNumber, LocalDate birthday, String resolution) {

        this.kakaoId = kakaoId;
        this.role = role;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.relationship = relationship;
        this.emergencyPhoneNumber = emergencyPhoneNumber;
        this.birthday = birthday;
        this.resolution = resolution;
    }

    public void update(String name, String phoneNumber, UserRoleEnum role, String profileImageUrl, String relationship, String emergencyPhoneNumber) {

        this.name = name;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.relationship = relationship;
        this.emergencyPhoneNumber= emergencyPhoneNumber;
    }

    public void update(String name, String phoneNumber, UserRoleEnum role, String profileImageUrl, LocalDate birthday, String resolution) {

        this.name = name;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.birthday = birthday;
        this.resolution = resolution;
    }
}
