package com.rdoneux.mslf.services;

import java.util.List;

import com.rdoneux.mslf.models.Poem;

public interface PoemService {
    List<Poem> findAll();
}
