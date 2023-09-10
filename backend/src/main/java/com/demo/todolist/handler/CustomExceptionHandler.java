package com.demo.todolist.handler;

import com.demo.todolist.dto.ResponseDto;
import com.demo.todolist.handler.ex.CustomApiException;
import com.demo.todolist.handler.ex.CustomValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    private static final String SERVER_ERROR_MSG = "관리자에게 문의하세요.";

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        log.error("[custom error] : {}", e);
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomValidationException.class )
    public ResponseEntity<?> validationException(CustomValidationException e) {
        log.error("[validation error] : {}", e);
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class )
    public ResponseEntity<?> serverException(Exception e) {
        log.error("[server error] : {}", e);
        return new ResponseEntity<>(new ResponseDto<>(-1, SERVER_ERROR_MSG, null), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
