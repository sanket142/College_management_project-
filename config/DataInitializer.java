package com.college.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.college.model.Course;
import com.college.model.User;
import com.college.repository.CourseRepository;
import com.college.repository.UserRepository;

@Component
@DependsOn("encoderConfig")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository   userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder  passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           CourseRepository courseRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository   = userRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder  = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (!userRepository.existsByEmail("admin@college.com")) {
            userRepository.save(User.builder()
                    .name("System Admin")
                    .email("admin@college.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(User.Role.ROLE_ADMIN)
                    .enabled(true)
                    .build());
            System.out.println("Admin created: admin@college.com / admin123");
        }

        if (!userRepository.existsByEmail("staff@college.com")) {
            userRepository.save(User.builder()
                    .name("Demo Staff")
                    .email("staff@college.com")
                    .password(passwordEncoder.encode("staff123"))
                    .role(User.Role.ROLE_STAFF)
                    .enabled(true)
                    .build());
            System.out.println("Staff created: staff@college.com / staff123");
        }

        if (courseRepository.count() == 0) {
            courseRepository.save(Course.builder().courseName("B.Tech Computer Science").fees(85000.0).duration("4 Years").totalSeats(60).availableSeats(60).active(true).build());
            courseRepository.save(Course.builder().courseName("B.Tech Electronics").fees(80000.0).duration("4 Years").totalSeats(60).availableSeats(60).active(true).build());
            courseRepository.save(Course.builder().courseName("B.Com").fees(45000.0).duration("3 Years").totalSeats(80).availableSeats(80).active(true).build());
            courseRepository.save(Course.builder().courseName("BBA").fees(50000.0).duration("3 Years").totalSeats(60).availableSeats(60).active(true).build());
            courseRepository.save(Course.builder().courseName("B.Sc Physics").fees(40000.0).duration("3 Years").totalSeats(40).availableSeats(40).active(true).build());
            System.out.println("5 sample courses seeded");
        }
    }
}
