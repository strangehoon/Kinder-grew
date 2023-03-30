package com.sparta.finalproject.domain.attendance.entity;

import com.sparta.finalproject.domain.child.entity.Child;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDate date;
    @Column
    private boolean entered;
    @Column
    private boolean exited;
    @Column
    private boolean absented;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;

    @Builder
    private Attendance (boolean isEntered, boolean isExited, boolean isAbsent, Child child){
        this.entered = isEntered;
        this.exited = isExited;
        this.absented = isAbsent;
        this.child = child;
        this.date = LocalDate.now();
    }

    public static Attendance from (Child child){
        return Attendance.builder()
                .isEntered(false)
                .isExited(false)
                .isAbsent(false)
                .child(child)
                .build();
    }

    public void enter(){
        entered = !entered;
    }

    public void exit(){
        exited = !exited;
    }
    public void absented(){
        absented = !absented;
    }
}
