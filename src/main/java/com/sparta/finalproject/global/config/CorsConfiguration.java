package com.sparta.finalproject.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

public class CorsConfiguration {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();

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
}
