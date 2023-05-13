package com.sparta.finalproject.domain.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        CustomStatusCode exception = (CustomStatusCode) request.getAttribute("exception");
        if (exception.equals(CustomStatusCode.ACCESS_TOKEN_EXPIRARION)) {
            exceptionHandler(response, CustomStatusCode.ACCESS_TOKEN_EXPIRARION);
        }
    }

    public void exceptionHandler (HttpServletResponse response, CustomStatusCode statusCode) {
        response.setStatus(statusCode.getStatusCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(GlobalResponseDto.from(CustomStatusCode.ACCESS_TOKEN_EXPIRARION));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}