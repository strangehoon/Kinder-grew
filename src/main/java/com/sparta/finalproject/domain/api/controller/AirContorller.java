package com.sparta.finalproject.domain.api.controller;


import com.sparta.finalproject.domain.api.entity.AirQuality;
import com.sparta.finalproject.domain.api.service.AirService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AirContorller {

    private final AirService airService;

    @GetMapping("/air")
    @ResponseBody
    public AirQuality getBadair() {
        return airService.testApi();
    }
}
