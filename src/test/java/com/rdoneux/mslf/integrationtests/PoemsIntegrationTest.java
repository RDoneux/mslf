package com.rdoneux.mslf.integrationtests;

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
import com.rdoneux.mslf.models.Poem;
import com.rdoneux.mslf.repositories.PoemRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PoemsIntegrationTest {

    private final String TARGET_ENDPOINT = "/poems";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PoemRepository poemRepository;

    @BeforeEach
    void cleanUp() {
        poemRepository.deleteAll();
    }

    Poem testPoem = Poem.builder().id("337e47a0-c3b8-4355-b190-0f523445c87c").title("test-poem-one-test-title")
            .author("test-poem-one-test-author").build();

    @Test
    void shouldReturn200WithListOfPoems() throws Exception {
        poemRepository.save(testPoem);

        MvcResult response = mockMvc
                .perform(get(TARGET_ENDPOINT)
                        .queryParam("page", "0")
                        .queryParam("size", "10"))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void shouldReturnPoemById() throws Exception {
        Poem savedPoem = poemRepository.save(testPoem);

        MvcResult response = mockMvc.perform(get(TARGET_ENDPOINT + "/" + savedPoem.getId())).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        String responseBody = response.getResponse().getContentAsString();
        String id = JsonPath.read(responseBody, "$.id");
        assertThat(id).isNotNull();
        assertThat(id).isEqualTo(savedPoem.getId());
    }

    @Test
    void shouldCreateNewPoem() throws Exception {
        MvcResult response = mockMvc
                .perform(post(TARGET_ENDPOINT).content(new ObjectMapper().writeValueAsString(testPoem))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
        String responseBody = response.getResponse().getContentAsString();
        String id = JsonPath.read(responseBody, "$.id");

        Optional<Poem> poem = poemRepository.findById(id);
        assertThat(poem.isPresent()).isTrue();
    }

    @Test
    void shouldUpdateExistingPoem() throws Exception {
        Poem savedPoem = poemRepository.save(testPoem);
        Poem updatedPoem = Poem.builder().title("updated-test-title").author("updated-test-author").build();

        MvcResult response = mockMvc.perform(
                put(TARGET_ENDPOINT + "/" + savedPoem.getId())
                        .content(new ObjectMapper().writeValueAsString(updatedPoem))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        Optional<Poem> persistedPoem = poemRepository.findById(savedPoem.getId());
        assertThat(persistedPoem.isPresent()).isTrue();
        assertThat(persistedPoem.get().getTitle()).isEqualTo(updatedPoem.getTitle());
        assertThat(persistedPoem.get().getAuthor()).isEqualTo(updatedPoem.getAuthor());
    }

    @Test
    void shouldDeletePoem() throws Exception {
        Poem savedPoem = poemRepository.save(testPoem);

        MvcResult response = mockMvc.perform(delete(TARGET_ENDPOINT + "/" + savedPoem.getId())).andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());

        Optional<Poem> shouldNotBePoem = poemRepository.findById(savedPoem.getId());
        assertThat(shouldNotBePoem.isEmpty()).isTrue();
    }
}
