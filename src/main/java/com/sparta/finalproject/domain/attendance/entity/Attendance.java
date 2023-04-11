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

import static com.sparta.finalproject.global.enumType.AttendanceStatus.결석;

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

    public static Attendance of (Child child, AttendanceStatus status){
        return Attendance.builder()
                .enterTime(null)
                .exitTime(null)
                .attendanceStatus(status)
                .localDate(LocalDate.now())
                .absentReason(null)
                .child(child)
                .build();
    }

    public static Attendance of (Child child, AttendanceStatus status, LocalDate startDate, String absentReason){
        return Attendance.builder()
                .enterTime(null)
                .exitTime(null)
                .attendanceStatus(status)
                .localDate(startDate)
                .child(child)
                .absentReason(absentReason)
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

    public void update(AttendanceStatus status, LocalTime enterTime, LocalTime exitTime, String absentReason){
        this.status = status;
        this.enterTime = enterTime;
        this.exitTime = exitTime;
        this.absentReason = absentReason;
    }

    public void update(AttendanceStatus status){
        this.status = status;
    }

    public void update(AttendanceStatus status, LocalTime exitTime){
        this.status = status;
        this.exitTime = exitTime;
    }

}
