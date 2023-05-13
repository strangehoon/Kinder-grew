package com.sparta.finalproject.domain.kindergarten.entity;

import com.sparta.finalproject.domain.kindergarten.dto.KindergartenRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Kindergarten {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String logoImageUrl;

    @Column
    private String kindergartenName;

    @Column
    private String contactNumber;

    @Column
    private String address;

    @Builder
    private Kindergarten (KindergartenRequestDto kindergartenRequestDto, String logoImageUrl){
        this.kindergartenName = kindergartenRequestDto.getKindergartenName();
        this.address = kindergartenRequestDto.getAddress();
        this.contactNumber = kindergartenRequestDto.getContactNumber();
        this.logoImageUrl = logoImageUrl;
    }

    public static Kindergarten of(KindergartenRequestDto kindergartenRequestDto, String logoImageUrl){
        return Kindergarten.builder()
                .kindergartenRequestDto(kindergartenRequestDto)
                .logoImageUrl(logoImageUrl)
                .build();
    }
}
