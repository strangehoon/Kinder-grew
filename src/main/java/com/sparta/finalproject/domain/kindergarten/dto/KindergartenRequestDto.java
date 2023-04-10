package com.sparta.finalproject.domain.kindergarten.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class KindergartenRequestDto {
    private String kindergartenName;
    private List<String> classroomList;
    private String address;
}
