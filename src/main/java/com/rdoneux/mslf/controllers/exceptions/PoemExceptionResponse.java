package com.rdoneux.mslf.controllers.exceptions;

import java.util.List;

import com.rdoneux.mslf.util.DefaultExceptionResponse;

public class PoemExceptionResponse <T> extends DefaultExceptionResponse<T> {
    public PoemExceptionResponse(String message, List<T> errors) {
        super(message, errors);
    }
}
