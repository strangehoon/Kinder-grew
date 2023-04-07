package com.sparta.finalproject.domain.classroom.entity;

import com.sparta.finalproject.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "classroom")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToOne
    @JoinColumn(name = "users_id")
    private User classroomTeacher;

    @Builder
    private Classroom(String name, User classroomTeacher){
        this.name = name;
        this.classroomTeacher = classroomTeacher;
    }

    public static Classroom from(String name) {
        return Classroom.builder()
                .name(name)
                .build();
    }

    public static Classroom of(String name, User classroomTeacher) {
        return Classroom.builder()
                .name(name)
                .classroomTeacher(classroomTeacher)
                .build();
    }

    public void update(User classroomTeacher) {
        this.classroomTeacher = classroomTeacher;
    }
}
