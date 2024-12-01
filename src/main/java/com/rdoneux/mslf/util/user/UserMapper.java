package com.rdoneux.mslf.util.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.rdoneux.mslf.models.User;
import com.rdoneux.mslf.models.UserDTO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserDTO toDTO(User user);

    @Mapping(target = "firstName", expression = "java(userDTO.getFullName().split(\" \")[0])")
    @Mapping(target = "lastName", expression = "java(userDTO.getFullName().split(\" \")[1])")
    User toUser(UserDTO userDTO, String id);

}
