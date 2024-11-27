package com.rdoneux.mslf.controllers.exceptions;

import lombok.Getter;

@Getter
public class ControllerValidationException extends RuntimeException {

    private final String field;

    public ControllerValidationException(String exceptionMessage, String field) {
        super(exceptionMessage);
        this.field = field;
    }

}
