package com.sparta.finalproject.global.response.exceptionType;


import com.sparta.finalproject.global.response.CustomStatusCode;
import lombok.Getter;

@Getter
public class ClassroomException extends GlobalException{

    public ClassroomException(CustomStatusCode statusCode){
        super(statusCode);
    }
}
