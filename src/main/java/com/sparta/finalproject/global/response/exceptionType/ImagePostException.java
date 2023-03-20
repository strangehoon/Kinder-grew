package com.sparta.finalproject.global.response.exceptionType;


import com.sparta.finalproject.global.response.CustomStatusCode;

public class ImagePostException extends GlobalException{

    public ImagePostException(CustomStatusCode statusCode){
        super(statusCode);
    }
}
