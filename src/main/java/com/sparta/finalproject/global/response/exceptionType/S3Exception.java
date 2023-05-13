package com.sparta.finalproject.global.response.exceptionType;


import com.sparta.finalproject.global.response.CustomStatusCode;

public class S3Exception extends GlobalException{

    public S3Exception(CustomStatusCode statusCode){
        super(statusCode);
    }
}
