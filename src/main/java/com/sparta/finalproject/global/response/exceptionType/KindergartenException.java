package com.sparta.finalproject.global.response.exceptionType;

import com.sparta.finalproject.global.response.CustomStatusCode;

public class KindergartenException extends GlobalException{

    public KindergartenException (CustomStatusCode statusCode){
        super(statusCode);
    }

}
