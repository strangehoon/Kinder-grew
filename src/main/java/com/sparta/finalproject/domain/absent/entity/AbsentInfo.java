package com.sparta.finalproject.domain.absent.entity;

import com.sparta.finalproject.domain.absent.dto.AbsentPostRequestDto;
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
    @Column(name = "absent_info")
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
    private AbsentInfo(AbsentPostRequestDto requestDto, Child child) {
        this.startDate = requestDto.getStartDate();
        this.endDate = requestDto.getEndDate();
        this.reason = requestDto.getReason();
        this.child = child;
    }

    public static AbsentInfo of(AbsentPostRequestDto requestDto, Child child) {
        return AbsentInfo.builder()
                .requestDto(requestDto)
                .child(child)
                .build();
    }
}
