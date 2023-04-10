package com.sparta.finalproject.domain.kindergarten.controller;

import com.sparta.finalproject.domain.kindergarten.dto.KindergartenRequestDto;
import com.sparta.finalproject.domain.kindergarten.service.KindergartenService;
import com.sparta.finalproject.domain.security.UserDetailsImpl;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class KindergartenController {

    private final KindergartenService kindergartenService;

    @PostMapping("kindergarten")
    public GlobalResponseDto kindergartenAdd(@RequestBody KindergartenRequestDto requestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails){
        return kindergartenService.addKindergarten(requestDto, userDetails.getUser());
    }

}
