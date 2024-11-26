package com.rdoneux.mslf.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rdoneux.mslf.controllers.exceptions.PoemEntityNotFoundException;
import com.rdoneux.mslf.models.Poem;
import com.rdoneux.mslf.models.PoemDTO;
import com.rdoneux.mslf.repositories.PoemRepository;
import com.rdoneux.mslf.util.poem.PoemMapper;

@Service
public class PoemServiceImpl implements PoemService {

    private final PoemRepository poemRepository;
    private final PoemMapper poemMapper;

    public PoemServiceImpl(PoemRepository poemRepository, PoemMapper poemMapper) {
        this.poemRepository = poemRepository;
        this.poemMapper = poemMapper;
    }

    @Override
    public List<PoemDTO> findAll() {
        List<Poem> poems = poemRepository.findAll();
        return poemMapper.toDTOList(poems);
    }

    @Override
    public Page<PoemDTO> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString("asc"), "createdAt"));
        Page<Poem> poems = poemRepository.findAll(pageable);
        return poems.map(poemMapper::toDTO);
    }

    @Override
    public PoemDTO findById(String id) {
        Optional<Poem> maybePoem = poemRepository.findById(id);

        if (maybePoem.isEmpty()) {
            throw new PoemEntityNotFoundException("Poem with id '" + id + "' not found");
        }

        return poemMapper.toDTO(maybePoem.get());
    }

    @Override
    public PoemDTO createPoem(PoemDTO poemDTO) {
        Poem poem = poemMapper.toPoem(poemDTO);
        Poem savedPoem = poemRepository.save(poem);
        System.out.println("saved poem id" + savedPoem.getId());
        return poemMapper.toDTO(savedPoem);
    }

}
