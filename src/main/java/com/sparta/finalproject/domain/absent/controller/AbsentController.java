package com.sparta.finalproject.domain.absent.controller;

import com.sparta.finalproject.domain.absent.dto.AbsentCancelRequestDto;
import com.sparta.finalproject.domain.absent.dto.AbsentAddRequestDto;
import com.sparta.finalproject.domain.absent.service.AbsentService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AbsentController {

    private final AbsentService absentService;

    // 결석 신청
    @PostMapping("api/parents/absent/{childId}")
    public GlobalResponseDto absentAdd(@PathVariable Long childId, @RequestBody AbsentAddRequestDto requestDto) {
        return absentService.addAbsent(childId, requestDto);
    }

    // 결석 취소
    @DeleteMapping("api/parents/absent/{childId}")
    public GlobalResponseDto absentRemove(@PathVariable Long childId, @RequestBody AbsentCancelRequestDto requestDto) {
        return absentService.removeAbsent(childId, requestDto);
    }
}
