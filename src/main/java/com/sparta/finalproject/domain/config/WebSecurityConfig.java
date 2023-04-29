package com.sparta.finalproject.domain.config;

import com.sparta.finalproject.domain.jwt.JwtAuthFilter;
import com.sparta.finalproject.domain.jwt.JwtUtil;
import com.sparta.finalproject.domain.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig{

    private final JwtUtil jwtUtil;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOriginPattern("*");

        config.addAllowedHeader("*");

        config.addAllowedMethod("*");

        config.addExposedHeader("Authorization");

        config.setAllowCredentials(true);

        config.validateAllowCredentials();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() { // password 암호화를 할때 BCrypt 방식을 사용해서 암호활를 하겠다는 의미
        return new BCryptPasswordEncoder();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // h2-console 사용 및 resources 접근 허용 설정
        return (web) -> web.ignoring()
//                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // 세션을 생성 하지도 않고 사용하지도 않겠다는 의미
        http.headers()
                .contentSecurityPolicy("default-src 'self' *.kakao.com *.kakao.co.kr *.kakaocdn.net *.daum.net *.daumcdn.net *.melon.co.kr *.melon.com *.google.com *.gstatic.com aem-collector.daumkakao.io aem-ingest.onkakao.net; img-src 'self' data:;");

        http.authorizeRequests()
                .antMatchers("/oauth/kakao/callback").permitAll()
//                .antMatchers("/parent/info").permitAll()
//                .antMatchers("/teacher/info").permitAll()
//                .antMatchers("/teacher").permitAll()
//                .antMatchers("/classroom/**").permitAll()
//                .antMatchers("/managers/**").permitAll()
//                .antMatchers("/manager/**").permitAll()
//                .antMatchers("/parent/**").permitAll()
//                .antMatchers("/api/**").permitAll()
//                .antMatchers("/child/**").permitAll()
//                .antMatchers("/kindergarten/**").permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                // JWT 인증/인가를 사용하기 위한 설정
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}



