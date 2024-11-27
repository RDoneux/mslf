package com.rdoneux.mslf.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.rdoneux.mslf.controllers.exceptions.EntityNotFoundException;
import com.rdoneux.mslf.models.Blog;
import com.rdoneux.mslf.models.BlogDTO;
import com.rdoneux.mslf.repositories.BlogRepository;
import com.rdoneux.mslf.util.blog.BlogMapper;

public class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private BlogMapper blogMapper;

    @InjectMocks
    private BlogServiceImpl blogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final Blog mockBlog = Blog.builder()
            .id("10191677-6c7f-49c5-9016-4c856537006f")
            .title("mock-blog-title")
            .author("mock-blog-author")
            .build();

    private final BlogDTO mockBlogDTO = BlogDTO.builder()
            .id("e5b745aa-9b79-4bf9-b552-f75ef62d3789")
            .title("mock-blogDTO-title")
            .author("mock-blogDTO-author")
            .build();

    /*
     **********
     * FIND ALL
     **********
     */
    @Test
    void shoudlReturnListOfBlogDTOFromRepository() {

        when(blogRepository.findAll()).thenReturn(List.of(mockBlog));
        when(blogMapper.toDTOList(List.of(mockBlog))).thenReturn(List.of(mockBlogDTO));

        List<BlogDTO> result = blogService.findAll();

        assertThat(result).hasSize(1);
        BlogDTO returnedBlog = result.get(0);
        assertThat(returnedBlog.getId()).isEqualTo(mockBlogDTO.getId());
        assertThat(returnedBlog.getTitle()).isEqualTo(mockBlogDTO.getTitle());
        assertThat(returnedBlog.getAuthor()).isEqualTo(mockBlogDTO.getAuthor());
    }

    /*
     *********************
     * FIND ALL PAGINATION
     *********************
     */

    @Test
    void shouldReturnBlogDTOPage() {
        Integer page = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString("asc"), "createdAt"));

        when(blogRepository.findAll(pageable)).thenReturn(new PageImpl<Blog>(List.of(mockBlog)));
        when(blogMapper.toDTO(mockBlog)).thenReturn(mockBlogDTO);

        Page<BlogDTO> result = blogService.findAll(page, size);

        List<BlogDTO> blogs = result.get().toList();
        assertThat(blogs).hasSize(1);
        assertThat(blogs.get(0).getId()).isEqualTo(mockBlogDTO.getId());

        verify(blogRepository, times(1)).findAll(pageable);
        verify(blogMapper, times(1)).toDTO(mockBlog);
    }

    /*
    *************
    * FIND BY ID
    *************
    */
    @Test
    void shouldReturnBlogDTOIfFound() {
        String id = "valid-id";

        when(blogRepository.findById(id)).thenReturn(Optional.of(mockBlog));
        when(blogMapper.toDTO(mockBlog)).thenReturn(mockBlogDTO);

        BlogDTO result = blogService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(mockBlogDTO.getId());

        verify(blogRepository, times(1)).findById(id);
        verify(blogMapper, times(1)).toDTO(mockBlog);
    }

    @Test
    void shouldThrowBlogEntityNotFoundExceptionIfNotFound() {
        String id = "not-found-id";

        when(blogRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> blogService.findById(id));

        verify(blogRepository, times(1)).findById(id);
        verify(blogMapper, times(0)).toDTO(mockBlog);
    }

    /*
    **************
    * CREATE BLOG
    **************
    */
    @Test
    void shouldReturnBlogDTOOnceSavedCreateBlog() {
        when(blogRepository.save(mockBlog)).thenReturn(mockBlog);
        when(blogMapper.toBlog(mockBlogDTO)).thenReturn(mockBlog);
        when(blogMapper.toDTO(mockBlog)).thenReturn(mockBlogDTO);

        BlogDTO result = blogService.createBlog(mockBlogDTO);

        assertThat(result.getId()).isEqualTo(mockBlogDTO.getId());

        verify(blogRepository, times(1)).save(mockBlog);
        verify(blogMapper, times(1)).toDTO(mockBlog);
        verify(blogMapper, times(1)).toBlog(mockBlogDTO);
    }

    /*
    **************
    * UPDATE BLOG
    **************
    */
    @Test
    void shouldReturnBlogDTOOnceSavedUpdateBlog() {
        String id = "test-id";

        when(blogRepository.save(any())).thenReturn(mockBlog);
        when(blogMapper.toBlog(any())).thenReturn(mockBlog);
        when(blogMapper.toDTO(mockBlog)).thenReturn(mockBlogDTO);

        BlogDTO result = blogService.updateBlog(id, mockBlogDTO);

        assertThat(result.getId()).isEqualTo(mockBlogDTO.getId());

        verify(blogRepository, times(1)).save(mockBlog);
        verify(blogMapper, times(1)).toDTO(mockBlog);
        verify(blogMapper, times(1)).toBlog(any());
    }

    /*
    **************
    * UPDATE BLOG
    **************
    */
    @Test
    void shouldCallDeleteByIdOnRepository() {

        String id = "test-id";

        blogService.deleteBlog(id);

        verify(blogRepository, times(1)).deleteById(id);
    }
}
