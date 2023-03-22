package com.sparta.finalproject.domain.parent.entity;

import com.sparta.finalproject.global.enumType.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String kakaoEmail;

    @Column
    private String kakaoId;

    @Column
    private String kakaoPassword;

    @Column
    private String name;

    @Column
    private String phoneNumber;

    @Column
    private String emergencyPhoneNumber;

    @Column
    private String email;
    @Column
    private String homePhone;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    private String profileImageUrl;


}
