package com.sparta.finalproject.global.response;


import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.exceptionType.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<GlobalResponseDto> handleUserException(UserException ex){
        CustomStatusCode statusCode = ex.getStatusCode();
        log.error(statusCode.getMessage());
        return ResponseEntity.badRequest().body(GlobalResponseDto.from(statusCode));
    }

    @ExceptionHandler(ImagePostException.class)
    public ResponseEntity<GlobalResponseDto> handleImagePostException(ImagePostException ex){
        CustomStatusCode statusCode = ex.getStatusCode();
        log.error(statusCode.getMessage());
        return ResponseEntity.badRequest().body(GlobalResponseDto.from(statusCode));
    }

    @ExceptionHandler(ClassroomException.class)
    public ResponseEntity<GlobalResponseDto> handleClassroomException(ClassroomException ex){
        CustomStatusCode statusCode = ex.getStatusCode();
        log.error(statusCode.getMessage());
        return ResponseEntity.badRequest().body(GlobalResponseDto.from(statusCode));
    }

    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<GlobalResponseDto> handleS3Exception(S3Exception ex){
        CustomStatusCode statusCode = ex.getStatusCode();
        log.error(statusCode.getMessage());
        return ResponseEntity.badRequest().body(GlobalResponseDto.from(statusCode));
    }

    @ExceptionHandler(ChildException.class)
    public ResponseEntity<GlobalResponseDto> handleChildException(ChildException ex){
        CustomStatusCode statusCode = ex.getStatusCode();
        log.error(statusCode.getMessage());
        return ResponseEntity.badRequest().body(GlobalResponseDto.from(statusCode));
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<GlobalResponseDto> handleDatetimeException(DateTimeException ex){
        CustomStatusCode statusCode = ex.getStatusCode();
        log.error(statusCode.getMessage());
        return ResponseEntity.badRequest().body(GlobalResponseDto.from(statusCode));
    }

    @ExceptionHandler(KindergartenException.class)
    public ResponseEntity<GlobalResponseDto> handleKindergartenException(KindergartenException ex){
        CustomStatusCode statusCode = ex.getStatusCode();
        log.error(statusCode.getMessage());
        return ResponseEntity.badRequest().body(GlobalResponseDto.from(statusCode));
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<GlobalResponseDto> handleGlobalException(GlobalException ex){
        CustomStatusCode statusCode = ex.getStatusCode();
        log.error(statusCode.getMessage());
        return ResponseEntity.badRequest().body(GlobalResponseDto.from(statusCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponseDto> handleMethodException(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error(message);
        return ResponseEntity.badRequest().body(new GlobalResponseDto(HttpStatus.BAD_REQUEST.value(), message, null));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GlobalResponseDto> handleDataBaseException(DataIntegrityViolationException ex){
        String message = ex.getRootCause().getMessage();
        List<String> returnMessage = List.of(message.split("'"));
        List<String> valueList = List.of(returnMessage.get(1).split("-"));
        List<String> keyList = List.of(returnMessage.get(3).split("_"));
        StringBuilder errorMessage = new StringBuilder();
        for (int i = 0; i < valueList.size(); i++) {
            errorMessage.append(keyList.get(i + 3)).append(":").append(valueList.get(i)).append(" 가 이미 존재합니다\n");
        }
        log.error(String.valueOf(errorMessage));
        return ResponseEntity.badRequest().body(new GlobalResponseDto(HttpStatus.BAD_REQUEST.value(), String.valueOf(errorMessage), null));
    }

    @ExceptionHandler(AttendanceException.class)
    public ResponseEntity<GlobalResponseDto> handleAttendanceException(AttendanceException ex){
        CustomStatusCode statusCode = ex.getStatusCode();
        log.error(statusCode.getMessage());
        return ResponseEntity.badRequest().body(GlobalResponseDto.from(statusCode));
    }

    @ExceptionHandler(AbsentException.class)
    public ResponseEntity<GlobalResponseDto> handleAbsentException(AbsentException ex){
        CustomStatusCode statusCode = ex.getStatusCode();
        log.error(statusCode.getMessage());
        return ResponseEntity.badRequest().body(GlobalResponseDto.from(statusCode));
    }
}
