package com.rdoneux.mslf.integrationtests;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.rdoneux.mslf.models.Blog;
import com.rdoneux.mslf.repositories.BlogRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BlogIntegrationTest {

 private final String TARGET_ENDPOINT = "/blogs";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogRepository blogRepository;

    @BeforeEach
    void cleanUp() {
        blogRepository.deleteAll();
    }

    Blog testBlog = Blog.builder().id("337e47a0-c3b8-4355-b190-0f523445c87c").title("test-blog-one-test-title")
            .author("test-blog-one-test-author").build();

    @Test
    void shouldReturn200WithListOfBlogs() throws Exception {
        blogRepository.save(testBlog);

        MvcResult response = mockMvc
                .perform(get(TARGET_ENDPOINT)
                        .queryParam("page", "0")
                        .queryParam("size", "10"))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void shouldReturnBlogById() throws Exception {
        Blog savedBlog = blogRepository.save(testBlog);

        MvcResult response = mockMvc.perform(get(TARGET_ENDPOINT + "/" + savedBlog.getId())).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        String responseBody = response.getResponse().getContentAsString();
        String id = JsonPath.read(responseBody, "$.id");
        assertThat(id).isNotNull();
        assertThat(id).isEqualTo(savedBlog.getId());
    }

    @Test
    void shouldCreateNewBlog() throws Exception {
        MvcResult response = mockMvc
                .perform(post(TARGET_ENDPOINT).content(new ObjectMapper().writeValueAsString(testBlog))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        String responseBody = response.getResponse().getContentAsString();
        String id = JsonPath.read(responseBody, "$.id");

        Optional<Blog> blog = blogRepository.findById(id);
        assertThat(blog.isPresent()).isTrue();
    }

    @Test
    void shouldUpdateExistingBlog() throws Exception {
        Blog savedBlog = blogRepository.save(testBlog);
        Blog updatedBlog = Blog.builder().title("updated-test-title").author("updated-test-author").build();

        MvcResult response = mockMvc.perform(
                put(TARGET_ENDPOINT + "/" + savedBlog.getId())
                        .content(new ObjectMapper().writeValueAsString(updatedBlog))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        Optional<Blog> persistedBlog = blogRepository.findById(savedBlog.getId());
        assertThat(persistedBlog.isPresent()).isTrue();
        assertThat(persistedBlog.get().getTitle()).isEqualTo(updatedBlog.getTitle());
        assertThat(persistedBlog.get().getAuthor()).isEqualTo(updatedBlog.getAuthor());
    }

    @Test
    void shouldDeleteBlog() throws Exception {
        Blog savedBlog = blogRepository.save(testBlog);

        MvcResult response = mockMvc.perform(delete(TARGET_ENDPOINT + "/" + savedBlog.getId())).andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        Optional<Blog> shouldNotBeBlog = blogRepository.findById(savedBlog.getId());
        assertThat(shouldNotBeBlog.isEmpty()).isTrue();
    }

}
