package com.rdoneux.mslf.controllers.exceptions;

import java.util.List;

import com.rdoneux.mslf.util.DefaultExceptionResponse;

public class ControllerExceptionResponse <T> extends DefaultExceptionResponse<T> {
    public ControllerExceptionResponse(String message, List<T> errors) {
        super(message, errors);
    }
}
