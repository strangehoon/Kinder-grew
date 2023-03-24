package com.sparta.finalproject.domain.absent.entity;

import com.sparta.finalproject.domain.absent.dto.AbsentAddRequestDto;
import com.sparta.finalproject.domain.child.entity.Child;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    private AbsentInfo(AbsentAddRequestDto requestDto, Child child) {
        this.startDate = requestDto.getStartDate();
        this.endDate = requestDto.getEndDate();
        this.reason = requestDto.getReason();
        this.child = child;
    }

    public static AbsentInfo of(AbsentAddRequestDto requestDto, Child child) {
        return AbsentInfo.builder()
                .requestDto(requestDto)
                .child(child)
                .build();
    }
}
