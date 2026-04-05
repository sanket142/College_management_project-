package com.college.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.college.model.Fees;
import com.college.model.Student;

public interface FeesRepository extends JpaRepository<Fees, Long> {
    List<Fees> findByStudent(Student student);
    List<Fees> findByStudent_Id(Long studentId);
    List<Fees> findByStatus(Fees.PaymentStatus status);

    @Query("SELECT COALESCE(SUM(f.amount),0) FROM Fees f WHERE f.status = 'PAID'")
    Double totalCollected();

    @Query("SELECT COALESCE(SUM(f.amount),0) FROM Fees f WHERE f.status = 'UNPAID'")
    Double totalPending();
}
