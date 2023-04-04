package com.sparta.finalproject.domain.attendance.entity;

import com.sparta.finalproject.domain.child.entity.Child;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

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
    private LocalTime enterTime;
    @Column
    private LocalTime exitTime;
    @Column
    private boolean absented;
    @Column
    private String absentReason;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;

    @Builder
    private Attendance (LocalTime enterTime, LocalTime exitTime, boolean absented, LocalDate localDate, String absentReason, Child child){
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.absented = absented;
        this.child = child;
        this.date = localDate;
        this.absentReason = absentReason;
    }

    public static Attendance from (Child child){
        return Attendance.builder()
                .enterTime(null)
                .exitTime(null)
                .absented(false)
                .localDate(LocalDate.now())
                .absentReason(null)
                .child(child)
                .build();
    }

//    public static Attendance of(Child child, LocalDate localDate, String absentReason){
//        return Attendance.builder()
//                .entered(false)
//                .exited(false)
//                .absented(true)
//                .localDate(localDate)
//                .absentReason(absentReason)
//                .child(child)
//                .build();
//    }

    public void enter(LocalTime enterTime){
        this.enterTime = enterTime;
    }

    public void exit(LocalTime exitTime){
        this.exitTime = exitTime;
    }

}
