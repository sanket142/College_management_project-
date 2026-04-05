package com.college.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.college.model.Staff;
import com.college.repository.StaffRepository;
import com.college.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service	
public class StaffService {

    private final StaffRepository staffRepository;
    private final UserRepository  userRepository;

    @Autowired
    public StaffService(StaffRepository staffRepository,
                        UserRepository userRepository) {
        this.staffRepository = staffRepository;
        this.userRepository  = userRepository;
    }

    public List<Staff> findAll() {
        return staffRepository.findAll();  // use standard findAll() unless findAll1() is custom
    }

    @Transactional
    public void delete(Long id) {
        staffRepository.findById(id).ifPresent(s -> {
            Long userId = s.getUser().getId();
            staffRepository.deleteById(id);
            userRepository.deleteById(userId);
        });
    }
}