package com.rdoneux.mslf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rdoneux.mslf.models.Poem;

public interface PoemRepository extends JpaRepository<Poem, String> {}
