package com.sparta.finalproject.domain.classroom.entity;

import com.sparta.finalproject.domain.classroom.dto.TeacherRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column
    private String name;
    @Column
    private int age;
    @Column
    private LocalDate birth;
    @Column
    private String phoneNumber;
    @Column
    private String license;
    @Column
    private String email;
    @Column
    private String resolution;
    @Column
    private String imageUrl;
    @ManyToOne
    private Classroom classroom;

    @Builder
    public Teacher(TeacherRequestDto teacherRequestDto, Classroom classroom){
        this.age = teacherRequestDto.getAge();
        this.birth =teacherRequestDto.getBirth();
        this.email = teacherRequestDto.getEmail();
        this.imageUrl = teacherRequestDto.getImage();
        this.license = teacherRequestDto.getLicense();
        this.name = teacherRequestDto.getName();
        this.resolution = teacherRequestDto.getResolution();
        this.phoneNumber = teacherRequestDto.getPhoneNumber();
        this.classroom = classroom;
    }
    public static Teacher of(TeacherRequestDto teacherRequestDto, Classroom classroom){
        return Teacher.builder()
                .teacherRequestDto(teacherRequestDto)
                .classroom(classroom)
                .build();
    }
}
