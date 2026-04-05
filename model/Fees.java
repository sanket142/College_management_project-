package com.college.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fees")
public class Fees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private Double amount;
    private String semester;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status = PaymentStatus.UNPAID;

    private LocalDate dueDate;
    private LocalDate paymentDate;
    private String    transactionId;
    private String    paymentMode;
    private String    description;

    public enum PaymentStatus { PAID, UNPAID, PARTIAL, OVERDUE }

    // ── no-arg constructor required by JPA ─────────
    public Fees() {}

    private Fees(Builder b) {
        this.student       = b.student;
        this.amount        = b.amount;
        this.semester      = b.semester;
        this.status        = b.status;
        this.dueDate       = b.dueDate;
        this.paymentDate   = b.paymentDate;
        this.transactionId = b.transactionId;
        this.paymentMode   = b.paymentMode;
        this.description   = b.description;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Student       student;
        private Double        amount;
        private String        semester;
        private PaymentStatus status = PaymentStatus.UNPAID;
        private LocalDate     dueDate;
        private LocalDate     paymentDate;
        private String        transactionId;
        private String        paymentMode;
        private String        description;

        public Builder student(Student v)           { this.student       = v; return this; }
        public Builder amount(Double v)             { this.amount        = v; return this; }
        public Builder semester(String v)           { this.semester      = v; return this; }
        public Builder status(PaymentStatus v)      { this.status        = v; return this; }
        public Builder dueDate(LocalDate v)         { this.dueDate       = v; return this; }
        public Builder paymentDate(LocalDate v)     { this.paymentDate   = v; return this; }
        public Builder transactionId(String v)      { this.transactionId = v; return this; }
        public Builder paymentMode(String v)        { this.paymentMode   = v; return this; }
        public Builder description(String v)        { this.description   = v; return this; }
        public Fees build()                         { return new Fees(this); }
    }

    // ── getters / setters ───────────────────────────
    public Long          getId()                           { return id; }
    public void          setId(Long id)                    { this.id = id; }
    public Student       getStudent()                      { return student; }
    public void          setStudent(Student student)       { this.student = student; }
    public Double        getAmount()                       { return amount; }
    public void          setAmount(Double amount)          { this.amount = amount; }
    public String        getSemester()                     { return semester; }
    public void          setSemester(String semester)      { this.semester = semester; }
    public PaymentStatus getStatus()                       { return status; }
    public void          setStatus(PaymentStatus status)   { this.status = status; }
    public LocalDate     getDueDate()                      { return dueDate; }
    public void          setDueDate(LocalDate dueDate)     { this.dueDate = dueDate; }
    public LocalDate     getPaymentDate()                  { return paymentDate; }
    public void          setPaymentDate(LocalDate v)       { this.paymentDate = v; }
    public String        getTransactionId()                { return transactionId; }
    public void          setTransactionId(String v)        { this.transactionId = v; }
    public String        getPaymentMode()                  { return paymentMode; }
    public void          setPaymentMode(String paymentMode){ this.paymentMode = paymentMode; }
    public String        getDescription()                  { return description; }
    public void          setDescription(String description){ this.description = description; }
}
