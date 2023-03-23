package com.sparta.finalproject.domain.child.entity;

import com.sparta.finalproject.domain.child.dto.AttendanceModifyRequestDto;
import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import com.sparta.finalproject.domain.parent.dto.ParentProfileModifyRequestDto;
import com.sparta.finalproject.domain.parent.entity.Parent;
import com.sparta.finalproject.global.enumType.Gender;
import com.sparta.finalproject.global.enumType.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "child")
@Getter
@NoArgsConstructor
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
    private LocalTime dailyEnterTime;
    @Column
    private LocalTime dailyExitTime;
    @Column
    private String profileImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Parent parent;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "kindergarten", nullable = false)
//    private Kindergarten kindergarten;

    @Builder
    public Child(ChildRequestDto requestDto, Classroom classroom, String profileImageUrl) {
        this.name = requestDto.getName();
        this.age = requestDto.getAge();
        this.birth = requestDto.getBirth();
        this.gender = requestDto.getGender();
        this.significant = requestDto.getSignificant();
        this.dailyEnterTime = requestDto.getDailyEnterTime();
        this.dailyExitTime = requestDto.getDailyExitTime();
        this.profileImageUrl = profileImageUrl;
        this.classroom = classroom;
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

    //부모껏도 포함 해야함
    public void update(ChildRequestDto requestDto, Classroom classroom) {
        this.name = requestDto.getName();
        this.age = requestDto.getAge();
        this.birth = requestDto.getBirth();
        this.gender = requestDto.getGender();
        this.significant = requestDto.getSignificant();
        this.dailyEnterTime = requestDto.getDailyEnterTime();
        this.dailyExitTime = requestDto.getDailyExitTime();
        this.classroom = classroom;
    }

    public void update(ChildRequestDto requestDto, Classroom classroom, String profileImageUrl) {
        this.name = requestDto.getName();
        this.age = requestDto.getAge();
        this.birth = requestDto.getBirth();
        this.gender = requestDto.getGender();
        this.significant = requestDto.getSignificant();
        this.dailyEnterTime = requestDto.getDailyEnterTime();
        this.dailyExitTime = requestDto.getDailyExitTime();
        this.profileImageUrl = profileImageUrl;
        this.classroom = classroom;
    }

    public void update(AttendanceModifyRequestDto requestDto){
        dailyEnterTime = requestDto.getDailyEnterTime();
        dailyExitTime = requestDto.getDailyExitTime();
    }

    public void update(ParentProfileModifyRequestDto requestDto, String childImageUrl) {
        name = requestDto.getChildName();
        birth = requestDto.getChildBirth();
        gender = requestDto.getChildGender();
        significant = requestDto.getSignificant();
        profileImageUrl = childImageUrl;
    }

}
