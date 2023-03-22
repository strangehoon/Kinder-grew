package com.sparta.finalproject.global.response.exceptionType;

import com.sparta.finalproject.global.response.CustomStatusCode;

public class ChildException extends GlobalException{

    public ChildException(CustomStatusCode statusCode){
        super(statusCode);
    }
}