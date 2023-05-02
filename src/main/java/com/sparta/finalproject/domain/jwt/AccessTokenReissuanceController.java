package com.sparta.finalproject.domain.jwt;

import com.sparta.finalproject.global.dto.GlobalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AccessTokenReissuanceController {

    private final AccessTokenReissuanceService accessTokenReissuanceService;

    @PostMapping("accessToken")
    public GlobalResponseDto accessTokenReissuance(@RequestParam Long userId, HttpServletResponse response) {

        return accessTokenReissuanceService.reissuanceAccessToken(userId, response);
    }
}
