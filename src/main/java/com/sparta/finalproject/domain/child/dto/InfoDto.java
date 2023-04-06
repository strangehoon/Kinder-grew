package com.sparta.finalproject.domain.child.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InfoDto {

    private long total;

    private long entered;

    private long notEntered;

    private long exited;
}
