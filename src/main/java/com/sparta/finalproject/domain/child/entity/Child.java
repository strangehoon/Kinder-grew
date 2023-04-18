package com.sparta.finalproject.domain.child.entity;

import com.sparta.finalproject.domain.attendance.entity.Attendance;
import com.sparta.finalproject.domain.child.dto.AttendanceModifyRequestDto;
import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.global.enumType.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private int age;
    @Column
    private LocalDate birth;
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column
    private String significant;
    @Column
    private String dailyEnterTime;
    @Column
    private String dailyExitTime;
    @Column
    private String profileImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id", nullable = false)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;

    @OneToMany(mappedBy = "child", cascade = CascadeType.REMOVE)
    private List<Attendance> attendanceList = new ArrayList<>();

//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "kindergarten", nullable = false)
//    private Kindergarten kindergarten;

    @Builder
    public Child(ChildRequestDto requestDto, Classroom classroom, String profileImageUrl,User user) {
        this.name = requestDto.getName();
        this.birth = requestDto.getBirth();
        this.gender = requestDto.getGender();
        this.significant = requestDto.getSignificant();
        this.dailyEnterTime = requestDto.getDailyEnterTime();
        this.dailyExitTime = requestDto.getDailyExitTime();
        this.profileImageUrl = profileImageUrl;
        this.classroom = classroom;
        this.user = user;
    }

    public static Child of(ChildRequestDto requestDto, Classroom classroom) {
        return Child.builder()
                .classroom(classroom)
                .requestDto(requestDto)
                .build();
    }

    public static Child of(ChildRequestDto requestDto, Classroom classroom, String profileImageUrl) {
        return Child.builder()
                .classroom(classroom)
                .requestDto(requestDto)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    public static Child of(ChildRequestDto requestDto, Classroom classroom, String profileImageUrl,User user) {
        return Child.builder()
                .classroom(classroom)
                .requestDto(requestDto)
                .profileImageUrl(profileImageUrl)
                .user(user)
                .build();
    }

    public void update(ChildRequestDto requestDto, String profileImageUrl) {
        this.name = requestDto.getName();
        this.birth = requestDto.getBirth();
        this.gender = requestDto.getGender();
        this.significant = requestDto.getSignificant();
        this.profileImageUrl = profileImageUrl;
    }

    public void update(ChildRequestDto requestDto, Classroom classroom, String profileImageUrl,User user) {
        this.name = requestDto.getName();
        this.birth = requestDto.getBirth();
        this.gender = requestDto.getGender();
        this.significant = requestDto.getSignificant();
        this.dailyEnterTime = requestDto.getDailyEnterTime();
        this.dailyExitTime = requestDto.getDailyExitTime();
        this.profileImageUrl = profileImageUrl;
        this.classroom = classroom;
        this.user = user;
    }

    public void update(AttendanceModifyRequestDto requestDto) {
        dailyEnterTime = requestDto.getDailyEnterTime();
        dailyExitTime = requestDto.getDailyExitTime();
    }
}
