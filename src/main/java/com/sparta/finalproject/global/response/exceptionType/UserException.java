package com.sparta.finalproject.global.response.exceptionType;


import com.sparta.finalproject.global.response.CustomStatusCode;

public class UserException extends GlobalException{

    public UserException(CustomStatusCode statusCode){
        super(statusCode);
    }
}
