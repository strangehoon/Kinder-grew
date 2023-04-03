package com.sparta.finalproject.global.dto;


import com.sparta.finalproject.global.response.CustomStatusCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GlobalResponseDto {
    private final int statusCode;
    private final String message;
    private final Object data;

    @Builder
    private GlobalResponseDto(CustomStatusCode statusCode, Object data) {
        this.statusCode = statusCode.getStatusCode();
        this.message = statusCode.getMessage();
        this.data = data;
    }

    public GlobalResponseDto(int statusCode, String message, Object data){
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static GlobalResponseDto of(CustomStatusCode statusCode, Object data){
        return GlobalResponseDto.builder()
                .statusCode(statusCode)
                .data(data)
                .build();
    }

    public static GlobalResponseDto from(CustomStatusCode statusCode){
        return GlobalResponseDto.builder()
                .statusCode(statusCode)
                .build();
    }
}
