package com.rdoneux.mslf.util;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class DefaultExceptionResponse<T> {
    private final String message;
    private final List<?> errors;
}
