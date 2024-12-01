package com.rdoneux.mslf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rdoneux.mslf.models.User;

public interface UserRepository extends JpaRepository<User, String> {}
