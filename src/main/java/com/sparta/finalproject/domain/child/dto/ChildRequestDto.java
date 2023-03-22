package com.sparta.finalproject.domain.child.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ChildRequestDto {
    private String name;
    private int age;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birth;
    private String gender;
    private String significant;
    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime dailyEnterTime;
    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime dailyExitTime;
}
