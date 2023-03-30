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
    private boolean isEntered;
    @Column
    private boolean isExited;
    @Column
    private boolean isAbsent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;

    @Builder
    private Attendance (boolean isEntered, boolean isExited, boolean isAbsent, Child child){
        this.isEntered = isEntered;
        this.isExited = isExited;
        this.isAbsent = isAbsent;
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
        isEntered = !isEntered;
    }

    public void exit(){
        isExited = !isExited;
    }
    public void absented(){
        isAbsent = !isAbsent;
    }
}
