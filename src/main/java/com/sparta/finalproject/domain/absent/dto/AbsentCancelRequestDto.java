package com.sparta.finalproject.domain.absent.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AbsentCancelRequestDto {

    List<String> absentList = new ArrayList<>();

}
