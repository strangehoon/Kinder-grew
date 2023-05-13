package com.sparta.finalproject.domain.attendance.entity;

import com.sparta.finalproject.domain.attendance.dto.AbsentAddRequestDto;
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
public class AbsentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;

    @Builder
    private AbsentInfo(LocalDate startDate, LocalDate endDate, String reason, Child child){
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.child = child;
    }

    public static AbsentInfo of(AbsentAddRequestDto absentAddRequestDto, Child child){
        return AbsentInfo.builder()
                .startDate(absentAddRequestDto.getStartDate())
                .endDate(absentAddRequestDto.getEndDate())
                .reason(absentAddRequestDto.getReason())
                .child(child)
                .build();
    }
}

