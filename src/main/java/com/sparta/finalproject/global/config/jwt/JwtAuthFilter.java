package com.sparta.finalproject.global.config.jwt;

import com.sparta.finalproject.global.response.CustomStatusCode;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);
        String requestURI = request.getRequestURI();

        log.info(requestURI);
// Add this line to skip token validation for /accessToken endpoint
        if ("/accessToken".equals(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                request.setAttribute("exception", CustomStatusCode.ACCESS_TOKEN_EXPIRARION);
                filterChain.doFilter(request, response);
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(token);
            setAuthentication(info.getSubject());
        }
        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String name) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(name);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }
}
