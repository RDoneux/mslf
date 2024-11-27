package com.rdoneux.mslf.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.rdoneux.mslf.models.BlogDTO;

public interface BlogService {
    List<BlogDTO> findAll();

    Page<BlogDTO> findAll(Integer page, Integer size);

    BlogDTO findById(String id);

    BlogDTO createBlog(BlogDTO blog);

    BlogDTO updateBlog(String id, BlogDTO blog);

    void deleteBlog(String id);
}
