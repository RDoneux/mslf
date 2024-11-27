package com.rdoneux.mslf.advice;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rdoneux.mslf.controllers.BlogController;
import com.rdoneux.mslf.controllers.PoemController;
import com.rdoneux.mslf.controllers.exceptions.EntityNotFoundException;
import com.rdoneux.mslf.controllers.exceptions.ControllerExceptionResponse;
import com.rdoneux.mslf.util.NotFoundError;
import com.rdoneux.mslf.util.ValidationError;

@ControllerAdvice(assignableTypes = { PoemController.class, BlogController.class })
public class CustomControllerAdvice {

        @ExceptionHandler({ EntityNotFoundException.class })
        public ResponseEntity<ControllerExceptionResponse<NotFoundError>> handlePoemNotFoundException(
                        EntityNotFoundException notFoundException) {
                var errors = List.of(new NotFoundError(notFoundException.getMessage()));
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ControllerExceptionResponse<NotFoundError>("Entity Not Found", errors));
        }

        @ExceptionHandler({ MissingServletRequestParameterException.class })
        public ResponseEntity<ControllerExceptionResponse<ValidationError>> handleMissingRequestParamaters(
                        MissingServletRequestParameterException missingParamsException) {
                var errors = List.of(new ValidationError("Bad Request", missingParamsException.getMessage()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ControllerExceptionResponse<>("Missing Paramaters", errors));
        }

        @ExceptionHandler({ MethodArgumentNotValidException.class })
        public ResponseEntity<ControllerExceptionResponse<String>> handleMalformedPoemBody(
                        MethodArgumentNotValidException malformedBodyException) {
                List<String> errors = malformedBodyException.getFieldErrors().stream()
                                .map((FieldError fieldError) -> fieldError.getField() + " "
                                                + fieldError.getDefaultMessage())
                                .collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ControllerExceptionResponse<String>("Entity Missing Fields", errors));
        }

        @ExceptionHandler({ HttpMessageNotReadableException.class })
        public ResponseEntity<ControllerExceptionResponse<ValidationError>> handleNoBody(
                        HttpMessageNotReadableException noBodyException) {
                var errors = List.of(new ValidationError("Bad Request", noBodyException.getMessage()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ControllerExceptionResponse<ValidationError>("Request Missing Body", errors));
        }

}
