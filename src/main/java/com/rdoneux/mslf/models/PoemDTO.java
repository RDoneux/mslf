package com.rdoneux.mslf.models;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class PoemDTO {

    @NotNull
    private final String title;

    @NotNull
    private final String author;

    private final String id;
    private final String content;
    private final Timestamp updatedAt;
    private final Timestamp createdAt;
}
