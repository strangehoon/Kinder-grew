package com.sparta.finalproject.global.response.exceptionType;

import com.sparta.finalproject.global.response.CustomStatusCode;

public class AbsentException extends GlobalException{
    public AbsentException(CustomStatusCode statusCode){
        super(statusCode);
    }
}
