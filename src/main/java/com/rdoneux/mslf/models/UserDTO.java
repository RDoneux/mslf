package com.rdoneux.mslf.models;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class UserDTO {

    @NotNull
    private final String fullName;

    @NotNull
    private final String tag;

    @NotNull
    private final String about;

    private final Timestamp updatedAt;
    private final Timestamp createdAt;

}
