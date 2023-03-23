package com.sparta.finalproject.domain.parent.controller;


import com.sparta.finalproject.domain.parent.dto.ParentPageResponseDto;
import com.sparta.finalproject.domain.parent.service.ParentService;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    // 학부모 프로필 조회
    // 일단은 param으로 받아왔습니다. 추후에 userDetailImpl로 바꿔야 합니다.
    @GetMapping("api/parents/{childId}")
    public GlobalResponseDto parentProfileFind(@RequestParam Long parentId, @PathVariable Long childId){
        return parentService.findParentProfile(parentId, childId);
    }
}
