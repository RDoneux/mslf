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

import com.rdoneux.mslf.controllers.PoemController;
import com.rdoneux.mslf.controllers.exceptions.PoemEntityNotFoundException;
import com.rdoneux.mslf.controllers.exceptions.PoemExceptionResponse;
import com.rdoneux.mslf.util.NotFoundError;
import com.rdoneux.mslf.util.ValidationError;

@ControllerAdvice(assignableTypes = PoemController.class)
public class PoemControllerAdvice {

        @ExceptionHandler({ PoemEntityNotFoundException.class })
        public ResponseEntity<PoemExceptionResponse<NotFoundError>> handlePoemNotFoundException(
                        PoemEntityNotFoundException notFoundException) {
                var errors = List.of(new NotFoundError(notFoundException.getMessage()));
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new PoemExceptionResponse<NotFoundError>("Poem not found", errors));
        }

        @ExceptionHandler({ MissingServletRequestParameterException.class })
        public ResponseEntity<PoemExceptionResponse<ValidationError>> handleMissingRequestParamaters(
                        MissingServletRequestParameterException missingParamsException) {
                var errors = List.of(new ValidationError("Bad Request", missingParamsException.getMessage()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new PoemExceptionResponse<>("Missing Paramaters", errors));
        }

        @ExceptionHandler({ MethodArgumentNotValidException.class })
        public ResponseEntity<PoemExceptionResponse<String>> handleMalformedPoemBody(
                        MethodArgumentNotValidException malformedBodyException) {
                List<String> errors = malformedBodyException.getFieldErrors().stream()
                                .map((FieldError fieldError) -> fieldError.getField() + " "
                                                + fieldError.getDefaultMessage())
                                .collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new PoemExceptionResponse<String>("Poem Missing Fields", errors));
        }

        @ExceptionHandler({ HttpMessageNotReadableException.class })
        public ResponseEntity<PoemExceptionResponse<ValidationError>> handleNoBody(
                        HttpMessageNotReadableException noBodyException) {
                var errors = List.of(new ValidationError("Bad Request", noBodyException.getMessage()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new PoemExceptionResponse<ValidationError>("Request Missing Body", errors));
        }

        // @ExceptionHandler({ HttpMessageNotReadableException.class })
        // public ResponseEntity<PoemExceptionResponse<ValidationError>>
        // handlePoemValidationException(
        // PoemControllerValidationException validationException) {
        // var errors = List.of(new ValidationError(validationException.getField(),
        // validationException.getMessage()));
        // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        // .body(new PoemExceptionResponse<ValidationError>("Poem validation failed",
        // errors));
        // }

}
