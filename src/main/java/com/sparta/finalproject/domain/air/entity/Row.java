package com.sparta.finalproject.domain.air.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Row {
    private Double PM10;
    private Double PM25;

    @JsonCreator
    public Row(@JsonProperty("PM10") Double PM10, @JsonProperty("PM25") Double PM25) {
        this.PM10 = PM10;
        this.PM25 = PM25;
    }
}
