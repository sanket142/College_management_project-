package com.college.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.college.model.Fees;
import com.college.model.Student;
import com.college.repository.FeesRepository;
import com.college.repository.StudentRepository;

@Service
public class FeesService {

    private final FeesRepository    feesRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public FeesService(FeesRepository feesRepository,
                       StudentRepository studentRepository) {
        this.feesRepository    = feesRepository;
        this.studentRepository = studentRepository;
    }

    public List<Fees> findByStudent(String email) {
        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found: " + email));
        return feesRepository.findByStudent(student);
    }

    public List<Fees> findByStudentId(Long studentId) {
        return feesRepository.findByStudent_Id(studentId);
    }

    public List<Fees>     findAll()             { return feesRepository.findAll(); }
    public List<Fees>     findUnpaid()          { return feesRepository.findByStatus(Fees.PaymentStatus.UNPAID); }
    public Optional<Fees> findById(Long id)     { return feesRepository.findById(id); }

    public double totalCollected() {
        Double v = feesRepository.totalCollected();
        return v != null ? v : 0.0;
    }

    public double totalPending() {
        Double v = feesRepository.totalPending();
        return v != null ? v : 0.0;
    }

    @Transactional
    public Fees payFees(Long feesId, String paymentMode) {
        return payFees(feesId, paymentMode, null);
    }

    @Transactional
    public Fees payFees(Long feesId, String paymentMode, String utrNumber) {
        Fees fees = feesRepository.findById(feesId)
                .orElseThrow(() -> new RuntimeException("Fee record not found (id=" + feesId + ")"));

        if (fees.getStatus() == Fees.PaymentStatus.PAID) {
            throw new RuntimeException("This fee has already been paid.");
        }

        fees.setStatus(Fees.PaymentStatus.PAID);
        fees.setPaymentDate(LocalDate.now());
        fees.setPaymentMode(paymentMode != null ? paymentMode : "BHIM UPI");

        if (utrNumber != null && !utrNumber.isBlank()) {
            fees.setTransactionId("UTR-" + utrNumber.trim());
        } else {
            fees.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        return feesRepository.save(fees);
    }

    @Transactional
    public Fees createFeeRecord(Long studentId, Double amount,
                                String semester, LocalDate dueDate) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found (id=" + studentId + ")"));
        Fees fees = Fees.builder()
                .student(student)
                .amount(amount)
                .semester(semester)
                .dueDate(dueDate)
                .status(Fees.PaymentStatus.UNPAID)
                .build();
        return feesRepository.save(fees);
    }
}
