package com.sparta.finalproject.domain.attendance.entity;

import com.sparta.finalproject.domain.child.entity.Child;
import com.sparta.finalproject.global.enumType.AttendanceStatus;
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
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;
    @Column
    private String absentReason;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;

    @Builder
    private Attendance (LocalTime enterTime, LocalTime exitTime, AttendanceStatus attendanceStatus, LocalDate localDate, String absentReason, Child child){
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.status = attendanceStatus;
        this.child = child;
        this.date = localDate;
        this.absentReason = absentReason;
    }

    public static Attendance from (Child child){
        return Attendance.builder()
                .enterTime(null)
                .exitTime(null)
                .attendanceStatus(null)
                .localDate(LocalDate.now())
                .absentReason(null)
                .child(child)
                .build();
    }

    public void enter(LocalTime enterTime, AttendanceStatus attendanceStatus){
        this.enterTime = enterTime;
        this.status = attendanceStatus;
    }

    public void exit(LocalTime exitTime, AttendanceStatus attendanceStatus){
        this.exitTime = exitTime;
        this.status = attendanceStatus;
    }

}
