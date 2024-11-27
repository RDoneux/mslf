package com.rdoneux.mslf.controllers.exceptions;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
