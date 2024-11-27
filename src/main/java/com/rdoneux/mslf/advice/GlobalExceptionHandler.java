package com.rdoneux.mslf.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException exception) {
       return new ResponseEntity<String>("NullPointerException occured: " + exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); 
    }

}
