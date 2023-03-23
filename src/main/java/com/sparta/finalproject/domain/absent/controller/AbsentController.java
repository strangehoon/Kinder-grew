package com.sparta.finalproject.domain.absent.controller;

import com.sparta.finalproject.domain.absent.dto.AbsentPostRequestDto;
import com.sparta.finalproject.domain.absent.dto.AbsentPostResponseDto;
import com.sparta.finalproject.domain.absent.service.AbsentService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AbsentController {

    private final AbsentService absentService;

    // 결석 신청
    @PostMapping("api/parents/absent/{childId}")
    public GlobalResponseDto absentAdd(@PathVariable Long childId, @RequestBody AbsentPostRequestDto requestDto) {
        return absentService.addAbsent(childId, requestDto);
    }
}
