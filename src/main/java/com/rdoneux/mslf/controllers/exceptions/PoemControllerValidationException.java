package com.rdoneux.mslf.controllers.exceptions;

import lombok.Getter;

@Getter
public class PoemControllerValidationException extends RuntimeException {

    private final String field;

    public PoemControllerValidationException(String exceptionMessage, String field) {
        super(exceptionMessage);
        this.field = field;
    }

}
