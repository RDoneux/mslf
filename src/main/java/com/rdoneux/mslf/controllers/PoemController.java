package com.rdoneux.mslf.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdoneux.mslf.models.Poem;
import com.rdoneux.mslf.services.PoemService;

@RestController
@RequestMapping("/poems")
public class PoemController {

    private PoemService poemService;

    public PoemController(PoemService poemService) {
        this.poemService = poemService;
    }

    @GetMapping("/")
    public List<Poem> getMethodName() {
        return poemService.findAll();
    }

}
