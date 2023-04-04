package com.sparta.finalproject.global.response.exceptionType;

import com.sparta.finalproject.global.response.CustomStatusCode;

public class AttendanceException extends GlobalException{
    public AttendanceException(CustomStatusCode statusCode){
        super(statusCode);
    }
}
