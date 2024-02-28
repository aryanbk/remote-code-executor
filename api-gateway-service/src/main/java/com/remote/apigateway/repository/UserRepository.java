package com.remote.apigateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.remote.apigateway.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}