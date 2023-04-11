package com.sparta.finalproject.domain.kindergarten.dto;

import com.sparta.finalproject.domain.kindergarten.entity.Kindergarten;
import lombok.Builder;
import lombok.Getter;

@Getter
public class KindergartenResponseDto {

    private Long id;
    private String name;
    private String address;

    @Builder
    private KindergartenResponseDto(Kindergarten kindergarten){
        this.id = kindergarten.getId();
        this.name = kindergarten.getKindergartenName();
        this.address = kindergarten.getAddress();
    }

    public static KindergartenResponseDto of(Kindergarten kindergarten){
        return KindergartenResponseDto.builder()
                .kindergarten(kindergarten)
                .build();
    }
}
