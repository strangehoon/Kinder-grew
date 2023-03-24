package com.sparta.finalproject.domain.parent.entity;

<<<<<<< HEAD
=======
import com.sparta.finalproject.domain.parent.dto.ParentProfileModifyRequestDto;
>>>>>>> 12c6198f2dd5fe684847a16b382a6e4f9da6b998
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
<<<<<<< HEAD
=======
    private String relationship;
    @Column
>>>>>>> 12c6198f2dd5fe684847a16b382a6e4f9da6b998
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column
    private String profileImageUrl;

<<<<<<< HEAD

=======
    public void update(ParentProfileModifyRequestDto requestDto, String parentImageUrl) {
        phoneNumber = requestDto.getPhoneNumber();
        relationship = requestDto.getRelationship();
        emergencyPhoneNumber = requestDto.getEmergencyPhoneNumber();
        homePhone = requestDto.getHomePhone();
        role = Role.PARENT;
        email = requestDto.getEmail();
        profileImageUrl = parentImageUrl;
    }
>>>>>>> 12c6198f2dd5fe684847a16b382a6e4f9da6b998
}
