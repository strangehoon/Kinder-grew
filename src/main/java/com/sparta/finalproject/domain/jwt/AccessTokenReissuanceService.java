//package com.sparta.finalproject.domain.jwt;
//
//import com.sparta.finalproject.domain.user.entity.User;
//import com.sparta.finalproject.global.dto.GlobalResponseDto;
//import com.sparta.finalproject.global.response.CustomStatusCode;
//import com.sparta.finalproject.global.response.exceptionType.UserException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletResponse;
//
//@Service
//@RequiredArgsConstructor
//public class AccessTokenReissuanceService {
//
//    private final JwtUtil jwtUtil;
//
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    @Transactional
//    public GlobalResponseDto reissuanceAccessToken(User user, HttpServletResponse response) {
//
//        RefreshToken isRefreshToken = refreshTokenRepository.findByKakaoId(user.getKakaoId()).orElseThrow(
//                () -> new UserException(CustomStatusCode.REFRESH_TOKEN_NOT_FOUND));
//
//        if(isRefreshToken != null) {
//
//            String newAccessToken = jwtUtil.createToken(user.getName(), user.getRole());
//
//            Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, newAccessToken.substring(7));
//            cookie.setPath("/");
//
//            response.addCookie(cookie);
//        }
//
//        return GlobalResponseDto.from(CustomStatusCode.ACCESS_TOKEN_REISSUANCE_SUCCESS);
//    }
//}
