package com.rdoneux.mslf.controllers.exceptions;

public class DatabaseConflictException extends RuntimeException {
    public DatabaseConflictException() {
        super("Multiple users detected");
    }
}
