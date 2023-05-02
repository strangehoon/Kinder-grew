package com.sparta.finalproject.global.config.refreshToken;

import com.sparta.finalproject.global.config.jwt.JwtUtil;
import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.domain.user.repository.UserRepository;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccessTokenReissuanceService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public GlobalResponseDto reissuanceAccessToken(Long userId, HttpServletResponse response) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserException(CustomStatusCode.USER_NOT_FOUND)
        );
        RefreshToken isRefreshToken = refreshTokenRepository.findByKakaoId(user.getKakaoId()).orElseThrow(
                () -> new UserException(CustomStatusCode.REFRESH_TOKEN_NOT_FOUND));
        log.info(isRefreshToken.getRefreshToken());
        if(isRefreshToken != null) {
            String newAccessToken = jwtUtil.createToken(user.getName(), user.getRole());
            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);

//            Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, newAccessToken.substring(7));
//            cookie.setPath("/");
//
//            response.addCookie(cookie);
        }
        return GlobalResponseDto.from(CustomStatusCode.ACCESS_TOKEN_REISSUANCE_SUCCESS);
    }
}
