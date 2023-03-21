package com.sparta.finalproject.domain.child.entity;

import com.sparta.finalproject.domain.child.dto.ChildRequestDto;
import com.sparta.finalproject.domain.classroom.entity.Classroom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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
    private String gender;
    @Column
    private String significant;
    @Column
    private LocalDate dailyEnterTime;
    @Column
    private LocalDate dailyExitTime;
    @Column
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent", nullable = false)
//    private Parent parent;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "kindergarten", nullable = false)
//    private Kindergarten kindergarten;

    @Builder
    public Child(ChildRequestDto requestDto, Classroom classroom) {
        this.name = requestDto.getName();
        this.age = requestDto.getAge();
        this.birth = requestDto.getBirth();
        this.gender = requestDto.getGender();
        this.significant = requestDto.getSignificant();
        this.dailyEnterTime = requestDto.getDailyEnterTime();
        this.dailyExitTime = requestDto.getDailyExitTime();
        this.imageUrl = requestDto.getImage();
        this.classroom = classroom;
    }

    public static Child of(ChildRequestDto requestDto, Classroom classroom) {
        return Child.builder()
                .requestDto(requestDto)
                .classroom(classroom)
                .build();
    }

    //부모껏도 포함 해야함
    public void update(ChildRequestDto requestDto) {
        this.name = requestDto.getName();
        this.age = requestDto.getAge();
        this.birth = requestDto.getBirth();
        this.gender = requestDto.getGender();
        this.significant = requestDto.getSignificant();
    }
}
