package com.college.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.college.dto.RegisterDTO;
import com.college.model.Staff;
import com.college.model.Student;
import com.college.model.User;
import com.college.repository.StaffRepository;
import com.college.repository.StudentRepository;
import com.college.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository    userRepository;
    private final StudentRepository studentRepository;
    private final StaffRepository   staffRepository;
    private final PasswordEncoder   passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       StaffRepository staffRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository    = userRepository;
        this.studentRepository = studentRepository;
        this.staffRepository   = staffRepository;
        this.passwordEncoder   = passwordEncoder;
    }

    @Transactional
    public User registerStudent(RegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(User.Role.ROLE_STUDENT)
                .enabled(true)
                .build();
        user = userRepository.save(user);
        Student student = Student.builder().user(user).build();
        studentRepository.save(student);
        return user;
    }

    @Transactional
    public User registerStaff(RegisterDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(User.Role.ROLE_STAFF)
                .enabled(true)
                .build();
        user = userRepository.save(user);
        Staff staff = Staff.builder().user(user).build();
        staffRepository.save(staff);
        return user;
    }

    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }
    public List<User>     findAll()                  { return userRepository.findAll(); }
    public void           deleteUser(Long id)        { userRepository.deleteById(id); }
    @Transactional
    public User registerStaffFull(RegisterDTO dto,
                                   String department,
                                   String designation,
                                   String employeeId,
                                   String phone) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered: " + dto.getEmail());
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(User.Role.ROLE_STAFF)
                .enabled(true)
                .build();
        user = userRepository.save(user);

        Staff staff = new Staff();
        staff.setUser(user);
        staff.setDepartment(department);
        staff.setDesignation(designation);
        staff.setEmployeeId(employeeId);
        staff.setPhone(phone);
        staffRepository.save(staff);

        return user;
    }
}
