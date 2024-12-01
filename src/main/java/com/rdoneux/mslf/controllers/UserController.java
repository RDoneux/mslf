package com.rdoneux.mslf.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdoneux.mslf.models.UserDTO;
import com.rdoneux.mslf.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserDTO getUser() {
        return userService.getUser();
    }

    @PutMapping
    public UserDTO updateUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

}
