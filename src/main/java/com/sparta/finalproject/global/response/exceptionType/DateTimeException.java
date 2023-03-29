package com.sparta.finalproject.global.response.exceptionType;

import com.sparta.finalproject.global.response.CustomStatusCode;

public class DateTimeException extends GlobalException{

    public DateTimeException(CustomStatusCode statusCode){
        super(statusCode);
    }
}
