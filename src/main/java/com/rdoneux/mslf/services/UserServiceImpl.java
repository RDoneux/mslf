package com.rdoneux.mslf.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rdoneux.mslf.controllers.exceptions.DatabaseConflictException;
import com.rdoneux.mslf.models.User;
import com.rdoneux.mslf.models.UserDTO;
import com.rdoneux.mslf.repositories.UserRepository;
import com.rdoneux.mslf.util.user.UserMapper;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO getUser() {
        return userMapper.toDTO(getUserFromRepo());
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User savedUser = getUserFromRepo();
        User userToSave = userMapper.toUser(userDTO, savedUser.getId());

        return userMapper.toDTO(userRepository.save(userToSave));
    }

    private User getUserFromRepo() {
        List<User> results = userRepository.findAll();

        if (results.size() > 1) {
            throw new DatabaseConflictException();
        }

        return results.get(0);
    }

}
