package com.college.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.college.model.Student;
import com.college.model.User;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
    Optional<Student> findByUser_Email(String email);
    List<Student> findByAdmissionStatus(Student.AdmissionStatus status);
    List<Student> findByCourse_Id(Long courseId);
    long countByAdmissionStatus(Student.AdmissionStatus status);

    @Query("SELECT s FROM Student s WHERE s.user.name LIKE %:q% OR s.user.email LIKE %:q% OR s.rollNumber LIKE %:q%")
    List<Student> search(String q);
}
