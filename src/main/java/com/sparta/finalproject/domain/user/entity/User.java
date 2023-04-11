package com.sparta.finalproject.domain.user.entity;

import com.sparta.finalproject.domain.kindergarten.entity.Kindergarten;
import com.sparta.finalproject.domain.user.dto.*;
import com.sparta.finalproject.global.enumType.UserRoleEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.Pattern;
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

    @Column(unique = true, length = 30)
    private String emergencyPhoneNumber;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column
    private String resolution;

    @Column
    @Pattern(regexp = "^[0-9a-zA-Z]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$")
    private String email;

    @ManyToOne
    private Kindergarten kindergarten;

    @Builder
    public User(KakaoUserRequestDto requestDto, UserRoleEnum role, String profileImageUrl) {
        this.kakaoId = requestDto.getKakaoId();
        this.role = role;
        this.name = requestDto.getName();
        this.profileImageUrl = profileImageUrl;
        this.email = null;
        this.phoneNumber = null;
        this.emergencyPhoneNumber = null;
        this.birthday = null;
        this.resolution = null;
        this.kindergarten = null;
    }

    public void update(ParentModifyRequestDto requestDto, UserRoleEnum role, String profileImageUrl) {

        this.name = requestDto.getName();
        this.role = role;
        this.phoneNumber = requestDto.getPhoneNumber();
        this.profileImageUrl = profileImageUrl;
        this.email = requestDto.getEmail();
        this.emergencyPhoneNumber= requestDto.getEmergencyPhoneNumber();
    }

    public void update(TeacherModifyRequestDto requestDto, UserRoleEnum role, String profileImageUrl) {

        this.name = requestDto.getName();
        this.role = role;
        this.phoneNumber = requestDto.getPhoneNumber();
        this.profileImageUrl = profileImageUrl;
        this.email = requestDto.getEmail();
        this.birthday = requestDto.getBirthday();
        this.resolution = requestDto.getResolution();
    }

    public void update(TeacherProfileModifyRequestDto requestDto, String profileImageUrl) {

        this.name = requestDto.getName();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.profileImageUrl = profileImageUrl;
        this.email = requestDto.getEmail();
        this.birthday = requestDto.getBirthday();
        this.resolution = requestDto.getResolution();
    }

    public void update(PrincipalModifyRequestDto requestDto, UserRoleEnum role, String profileImageUrl) {
        this.name = requestDto.getName();
        this.phoneNumber = requestDto.getPhoneNumber();
        this.email = requestDto.getEmail();
        this.birthday = requestDto.getBirthday();
        this.role = role;
        this.profileImageUrl = profileImageUrl;
    }

    public void setKindergarten(Kindergarten kindergarten){
        this.kindergarten = kindergarten;
    }

    public void setRole(UserRoleEnum role){
        this.role = role;
    }

    public void clear(){
        this.role = UserRoleEnum.EARLY_USER;
        this.email = null;
        this.phoneNumber = null;
        this.emergencyPhoneNumber = null;
        this.birthday = null;
        this.resolution = null;
        this.kindergarten = null;
    }
}
