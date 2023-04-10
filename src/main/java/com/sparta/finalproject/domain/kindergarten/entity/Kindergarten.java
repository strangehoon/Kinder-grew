package com.sparta.finalproject.domain.kindergarten.entity;

import com.sparta.finalproject.domain.kindergarten.dto.KindergartenRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Kindergarten {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String kindergartenName;

    @Column
    private String address;

    @Column
    private String teacherCode;

    @Column
    private String parentCode;

    @Builder
    private Kindergarten (KindergartenRequestDto kindergartenRequestDto){
        this.kindergartenName = kindergartenRequestDto.getKindergartenName();
        this.address = kindergartenRequestDto.getAddress();
        this.teacherCode = RandomStringUtils.randomAlphanumeric(10);
        this.parentCode = RandomStringUtils.randomAlphanumeric(10);
    }

    public static Kindergarten from(KindergartenRequestDto kindergartenRequestDto){
        return Kindergarten.builder()
                .kindergartenRequestDto(kindergartenRequestDto)
                .build();
    }
}
