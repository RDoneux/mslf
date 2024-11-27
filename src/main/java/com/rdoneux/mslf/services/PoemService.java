package com.rdoneux.mslf.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rdoneux.mslf.models.PoemDTO;

public interface PoemService {
    List<PoemDTO> findAll();

    Page<PoemDTO> findAll(Integer page, Integer size);

    PoemDTO findById(String id);

    PoemDTO createPoem(PoemDTO poem);

    PoemDTO updatePoem(String id, PoemDTO poem);

    void deletePoem(String id);
}
