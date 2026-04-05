package com.college.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.college.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByRole(User.Role role);
}
