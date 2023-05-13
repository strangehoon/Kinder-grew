package com.sparta.finalproject.domain.kindergarten.controller;

import com.sparta.finalproject.domain.kindergarten.dto.KindergartenRequestDto;
import com.sparta.finalproject.domain.kindergarten.service.KindergartenService;
import com.sparta.finalproject.domain.security.UserDetailsImpl;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class KindergartenController {

    private final KindergartenService kindergartenService;

    // 유치원 정보 입력
    @PostMapping("kindergarten")
    public GlobalResponseDto kindergartenAdd(@ModelAttribute KindergartenRequestDto requestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return kindergartenService.addKindergarten(requestDto, userDetails.getUser());
    }

    // 유치원 검색
    @GetMapping("search/kindergarten")
    public GlobalResponseDto kindergartenFind(@RequestParam String keyword, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return kindergartenService.findKindergarten(keyword, userDetails.getUser());
    }

    // 유치원 선택
    @PutMapping("kindergarten/{kindergartenId}")
    public GlobalResponseDto kindergartenSelect(@PathVariable Long kindergartenId,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails){
        return kindergartenService.selectKindergarten(kindergartenId, userDetails.getUser());
    }

}
