package com.sparta.finalproject.global.response.exceptionType;

import com.sparta.finalproject.global.response.CustomStatusCode;

public class ParentException extends GlobalException{
    public ParentException(CustomStatusCode statusCode){
        super(statusCode);
    }
}
