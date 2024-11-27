package com.rdoneux.mslf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rdoneux.mslf.models.Blog;

public interface BlogRepository extends JpaRepository<Blog, String> {

}
