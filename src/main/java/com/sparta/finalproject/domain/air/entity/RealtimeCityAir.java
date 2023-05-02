package com.sparta.finalproject.domain.air.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RealtimeCityAir {
    private List<Row> row;

    public RealtimeCityAir(List<Row> row) {
        this.row = row;
    }
}
