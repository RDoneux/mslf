package com.rdoneux.mslf.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class NotFoundError {

    private final String error;

}
