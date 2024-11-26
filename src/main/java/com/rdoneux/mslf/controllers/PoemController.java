package com.rdoneux.mslf.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rdoneux.mslf.models.PoemDTO;
import com.rdoneux.mslf.services.PoemService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/poems")
public class PoemController {

    private final PoemService poemService;

    public PoemController(PoemService poemService) {
        this.poemService = poemService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<PoemDTO> getAllPoems(@RequestParam Integer page, @RequestParam Integer size) {
        return poemService.findAll(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PoemDTO getPoemById(@PathVariable String id) {
        return poemService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PoemDTO createPoem(@RequestBody @Valid PoemDTO poemDTO) {
        return poemService.createPoem(poemDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PoemDTO updatePoem(@PathVariable String id, @RequestBody @Valid PoemDTO poemDTO) {
        return poemService.updatePoem(id, poemDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePoem(@PathVariable String id) {
        poemService.deletePoem(id);
    }

}
