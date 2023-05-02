package com.sparta.finalproject.domain.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.finalproject.domain.api.entity.RealtimeCityAir;
import com.sparta.finalproject.domain.api.service.AirService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AirController {

    private final AirService airService;

    @GetMapping("/air")
    public RealtimeCityAir getBadair() throws JsonProcessingException {
        return airService.testAir();
    }
}
