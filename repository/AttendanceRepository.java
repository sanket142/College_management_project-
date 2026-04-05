package com.college.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.college.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudent_Id(Long studentId);
    List<Attendance> findByCourse_Id(Long courseId);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :sid AND a.status = 'PRESENT'")
    long countPresent(Long sid);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :sid")
    long countTotal(Long sid);
}
