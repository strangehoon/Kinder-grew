package com.sparta.finalproject.global.response.exceptionType;


import com.sparta.finalproject.global.response.CustomStatusCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GlobalException extends RuntimeException{
    private CustomStatusCode statusCode;

    public GlobalException (CustomStatusCode statusCode){
        this.statusCode = statusCode;
    }
}
