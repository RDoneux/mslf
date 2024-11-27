package com.rdoneux.mslf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import com.rdoneux.mslf.models.BlogDTO;
import com.rdoneux.mslf.services.BlogService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/blogs")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BlogDTO> getAllBlogs(@RequestParam @NotNull @Min(0) Integer page,
            @RequestParam @NotNull @Min(1) Integer size) {
        return blogService.findAll(page, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BlogDTO getBlogById(@PathVariable String id) {
        return blogService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlogDTO createBlog(@RequestBody @Valid BlogDTO blogDTO) {
        return blogService.createBlog(blogDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BlogDTO updateBlog(@PathVariable String id, @RequestBody @Valid BlogDTO blogDTO) {
        return blogService.updateBlog(id, blogDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlog(@PathVariable String id) {
        blogService.deleteBlog(id);
    }
}
