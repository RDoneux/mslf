package com.rdoneux.mslf.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdoneux.mslf.models.UserDTO;
import com.rdoneux.mslf.services.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String TEST_ENDPOINT = "/user";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn400BadRequestWhenBodyIsMissing() throws Exception {
        MvcResult response = mockMvc.perform(put(TEST_ENDPOINT)).andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturn400BadRequestWhenBodyIsInvalid() throws Exception {
        MvcResult response = mockMvc
                .perform(put(TEST_ENDPOINT).content(new ObjectMapper().writeValueAsString(UserDTO.builder().build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
