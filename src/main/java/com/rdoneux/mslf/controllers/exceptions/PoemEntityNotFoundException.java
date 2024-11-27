package com.rdoneux.mslf.controllers.exceptions;

import lombok.Getter;

@Getter
public class PoemEntityNotFoundException extends RuntimeException {
    public PoemEntityNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
