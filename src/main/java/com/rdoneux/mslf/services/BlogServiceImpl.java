package com.rdoneux.mslf.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rdoneux.mslf.controllers.exceptions.EntityNotFoundException;
import com.rdoneux.mslf.models.Blog;
import com.rdoneux.mslf.models.BlogDTO;
import com.rdoneux.mslf.repositories.BlogRepository;
import com.rdoneux.mslf.util.blog.BlogMapper;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogMapper blogMapper;

    public BlogServiceImpl(BlogRepository blogRepository, BlogMapper blogMapper) {
        this.blogRepository = blogRepository;
        this.blogMapper = blogMapper;
    }

    @Override
    public List<BlogDTO> findAll() {
        List<Blog> blogs = blogRepository.findAll();
        return blogMapper.toDTOList(blogs);
    }

    @Override
    public Page<BlogDTO> findAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString("asc"), "createdAt"));
        Page<Blog> blogs = blogRepository.findAll(pageable);
        return blogs.map(blogMapper::toDTO);
    }

    @Override
    public BlogDTO findById(String id) {
        Optional<Blog> maybeBlog = blogRepository.findById(id);

        if (maybeBlog.isEmpty()) {
            throw new EntityNotFoundException("Blog with id '" + id + "' not found");
        }

        return blogMapper.toDTO(maybeBlog.get());
    }

    @Override
    public BlogDTO createBlog(BlogDTO blogDTO) {
        Blog blog = blogMapper.toBlog(blogDTO);
        Blog savedBlog = blogRepository.save(blog);
        return blogMapper.toDTO(savedBlog);
    }

    @Override
    public BlogDTO updateBlog(String id, BlogDTO blogDTO) {
        Blog blogToSave = blogMapper.toBlog(blogDTO.toBuilder().id(id).build());
        Blog savedBlog = blogRepository.save(blogToSave);
        return blogMapper.toDTO(savedBlog);
    }

    @Override
    public void deleteBlog(String id) {
        blogRepository.deleteById(id);
    }

}
