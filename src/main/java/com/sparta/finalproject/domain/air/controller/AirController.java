package com.sparta.finalproject.domain.air.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.finalproject.domain.air.entity.RealtimeCityAir;
import com.sparta.finalproject.domain.air.service.AirService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AirController {

    private final AirService airService;

    // 미세 먼지 API
    @GetMapping("/air")
    public RealtimeCityAir getAirCondition() throws JsonProcessingException {
        return airService.airConditionGet();
    }
}
