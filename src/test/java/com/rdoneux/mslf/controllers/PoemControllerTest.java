package com.rdoneux.mslf.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.rdoneux.mslf.services.PoemService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@WebMvcTest(PoemController.class)
public class PoemControllerTest {

    private static final String TEST_ENDPOINT = "/poems";

    @MockBean
    private PoemService poemService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn400BadRequestWhenPathVariableIsMissing() throws Exception {
        MvcResult response = mockMvc.perform(get(TEST_ENDPOINT)
                .queryParam("page", "0"))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        response = mockMvc.perform(get(TEST_ENDPOINT)
                .queryParam("size", "10"))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturn400BadRequestWhenPathVariableIsEmpty() throws Exception {
        MvcResult response = mockMvc.perform(get(TEST_ENDPOINT)
                .queryParam("page", "")
                .queryParam("size", "10"))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        response = mockMvc.perform(get(TEST_ENDPOINT)
                .queryParam("page", "0")
                .queryParam("size", ""))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturn400BadRequestWhenPageisLessThan0() throws Exception {
        MvcResult response = mockMvc.perform(get(TEST_ENDPOINT)
                .queryParam("page", "-1")
                .queryParam("size", "10"))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturn400BadRequestWhenSizeIsLessThan1() throws Exception {
        MvcResult response = mockMvc.perform(get(TEST_ENDPOINT)
                .queryParam("page", "0")
                .queryParam("size", "0"))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
