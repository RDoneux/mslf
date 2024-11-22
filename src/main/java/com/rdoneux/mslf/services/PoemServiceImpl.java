package com.rdoneux.mslf.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rdoneux.mslf.models.Poem;
import com.rdoneux.mslf.repositories.PoemRepository;

@Service
public class PoemServiceImpl implements PoemService {

    private PoemRepository poemRepository;

    public PoemServiceImpl(PoemRepository poemRepository) {
        this.poemRepository = poemRepository;
    }

    @Override
    public List<Poem> findAll() {
        return poemRepository.findAll();
    }
    
}
