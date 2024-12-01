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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.rdoneux.mslf.models.User;
import com.rdoneux.mslf.models.UserDTO;
import com.rdoneux.mslf.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserIntegrationTest {

    private final String TARGET_ENDPOINT = "/user";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    private final User testUser = User.builder().id("9e03783f-5ce9-4a65-935b-14666c80cc3d")
            .firstName("test-user-first-name")
            .lastName("test-user-last-name").tag("test-user-tag").about("test-user-about").build();

    @Test
    void shouldReturn200WithUser() throws Exception {
        User savedUser = userRepository.save(testUser);

        MvcResult response = mockMvc.perform(get(TARGET_ENDPOINT)).andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        String fullName = JsonPath.read(response.getResponse().getContentAsString(), "$.fullName");
        assertThat(fullName).isNotNull();
        assertThat(fullName).isEqualTo(savedUser.getFirstName() + " " + savedUser.getLastName());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        User savedUser = userRepository.save(testUser);
        UserDTO updatedUser = UserDTO.builder().fullName("user-update test-full-name")
                .tag("user-update-test-tag").about("user-update-test-about")
                .build();

        MvcResult response = mockMvc
                .perform(put(TARGET_ENDPOINT).content(
                        new ObjectMapper().writeValueAsString(updatedUser)).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        Optional<User> persistedUser = userRepository.findById(savedUser.getId());
        assertThat(persistedUser.isPresent()).isTrue();
        assertThat(persistedUser.get().getFirstName() + " " + persistedUser.get().getLastName())
                .isEqualTo(updatedUser.getFullName());
        assertThat(persistedUser.get().getTag()).isEqualTo(updatedUser.getTag());
        assertThat(persistedUser.get().getAbout()).isEqualTo(updatedUser.getAbout());
    }

}
