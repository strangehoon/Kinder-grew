package com.sparta.finalproject.domain.classroom.entity;

import com.sparta.finalproject.domain.kindergarten.entity.Kindergarten;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User classroomTeacher;

    @ManyToOne
    @JoinColumn(name = "kindergarten_id")
    private Kindergarten kindergarten;

    @Builder
    private Classroom(String name, User classroomTeacher, Kindergarten kindergarten){
        this.name = name;
        this.classroomTeacher = classroomTeacher;
        this.kindergarten = kindergarten;
    }

    public static Classroom of(String name, Kindergarten kindergarten) {
        return Classroom.builder()
                .name(name)
                .kindergarten(kindergarten)
                .build();
    }

    public static Classroom of(String name, User classroomTeacher, Kindergarten kindergarten) {
        return Classroom.builder()
                .name(name)
                .classroomTeacher(classroomTeacher)
                .kindergarten(kindergarten)
                .build();
    }

    public void update(User classroomTeacher) {
        this.classroomTeacher = classroomTeacher;
    }
}
