package com.rdoneux.mslf.services;

import com.rdoneux.mslf.models.UserDTO;

public interface UserService {

    UserDTO getUser();
    UserDTO updateUser(UserDTO user);
}
