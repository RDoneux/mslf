package com.rdoneux.mslf.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rdoneux.mslf.controllers.exceptions.DatabaseConflictException;
import com.rdoneux.mslf.models.User;
import com.rdoneux.mslf.models.UserDTO;
import com.rdoneux.mslf.repositories.UserRepository;
import com.rdoneux.mslf.util.user.UserMapper;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private final User mockUser = User.builder().id("29dbca59-1df0-4e8d-998b-34111268d1d4")
            .firstName("mock-user-first-name").lastName("mock-user-last-name").tag("mock-user-tag")
            .about("mock-user-about").build();

    private final UserDTO mockUserDTO = UserDTO.builder().fullName("mock-userDTO-full-name").tag("mock-userDTO-tag")
            .about("mock-userDTO-about").build();

    @Test
    void shouldReturnUserDTOFromRepository() throws Exception {

        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        when(userMapper.toDTO(mockUser)).thenReturn(mockUserDTO);

        UserDTO result = userService.getUser();

        assertThat(result).isNotNull();
        assertThat(result.getFullName()).isEqualTo(mockUserDTO.getFullName());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDTO(mockUser);
    }

    @Test
    void shouldThrowDatabaseConflictIfThereAreMoreThanTwoUsers() throws Exception {

        when(userRepository.findAll()).thenReturn(List.of(mockUser, mockUser));

        assertThrows(DatabaseConflictException.class, () -> userService.getUser());
        assertThrows(DatabaseConflictException.class, () -> userService.updateUser(mockUserDTO));

        verify(userRepository, times(2)).findAll();
    }

    @Test
    void shouldRequestUserIsUpdated() throws Exception {

        when(userRepository.findAll()).thenReturn(List.of(mockUser));
        when(userMapper.toUser(mockUserDTO, mockUser.getId())).thenReturn(mockUser);
        when(userMapper.toDTO(mockUser)).thenReturn(mockUserDTO);
        when(userRepository.save(any())).thenReturn(mockUser);

        UserDTO savedUser = userService.updateUser(mockUserDTO);

        assertThat(savedUser.getFullName()).isEqualTo(mockUserDTO.getFullName());

        verify(userRepository, times(1)).findAll();
        verify(userRepository, times(1)).save(any());
        verify(userMapper, times(1)).toUser(mockUserDTO, mockUser.getId());

    }

}
