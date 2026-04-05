package com.college.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.college.model.Staff;
import com.college.model.User;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByUser(User user);
    Optional<Staff> findByUser_Email(String email);
}
