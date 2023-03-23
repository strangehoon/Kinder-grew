package com.sparta.finalproject.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080",
                        "https://sparta-ys.shop",
                        "http://52.78.28.93:8080",
                        "http://localhost:3000",
                        "https://localhost:3000",
                        "https://127.0.0.1:3000"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
        ;

    }

}