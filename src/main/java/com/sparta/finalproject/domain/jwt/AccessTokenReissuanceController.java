//package com.sparta.finalproject.domain.jwt;
//
//import com.sparta.finalproject.domain.security.UserDetailsImpl;
//import com.sparta.finalproject.global.dto.GlobalResponseDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//import javax.servlet.http.HttpServletResponse;
//
//@RestController
//@RequiredArgsConstructor
//public class AccessTokenReissuanceController {
//
//    private final AccessTokenReissuanceService accessTokenReissuanceService;
//
//    @PostMapping("accessToken")
//    public GlobalResponseDto accessTokenReissuance(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
//
//        return accessTokenReissuanceService.reissuanceAccessToken(userDetails.getUser(), response);
//    }
//}
