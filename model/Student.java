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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    private AdmissionStatus admissionStatus = AdmissionStatus.PENDING;

    private String  documentPath;
    private String  phone;
    private String  address;
    private LocalDate dateOfBirth;
    private String  gender;
    private String  guardianName;
    private String  guardianPhone;
    private String  previousCollege;
    private Double  previousPercentage;
    private String  rollNumber;

    public enum AdmissionStatus { PENDING, APPROVED, REJECTED, WAITLISTED }

    // ── no-arg constructor required by JPA ─────────
    public Student() {}

    private Student(Builder b) {
        this.user               = b.user;
        this.course             = b.course;
        this.admissionStatus    = b.admissionStatus;
        this.documentPath       = b.documentPath;
        this.phone              = b.phone;
        this.address            = b.address;
        this.dateOfBirth        = b.dateOfBirth;
        this.gender             = b.gender;
        this.guardianName       = b.guardianName;
        this.guardianPhone      = b.guardianPhone;
        this.previousCollege    = b.previousCollege;
        this.previousPercentage = b.previousPercentage;
        this.rollNumber         = b.rollNumber;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private User user;
        private Course course;
        private AdmissionStatus admissionStatus = AdmissionStatus.PENDING;
        private String documentPath, phone, address, gender, guardianName,
                       guardianPhone, previousCollege, rollNumber;
        private LocalDate dateOfBirth;
        private Double previousPercentage;

        public Builder user(User v)                       { this.user = v; return this; }
        public Builder course(Course v)                   { this.course = v; return this; }
        public Builder admissionStatus(AdmissionStatus v) { this.admissionStatus = v; return this; }
        public Builder documentPath(String v)             { this.documentPath = v; return this; }
        public Builder phone(String v)                    { this.phone = v; return this; }
        public Student build()                            { return new Student(this); }
    }

    // ── getters / setters ───────────────────────────
    public Long             getId()                                  { return id; }
    public void             setId(Long id)                           { this.id = id; }
    public User             getUser()                                { return user; }
    public void             setUser(User user)                       { this.user = user; }
    public Course           getCourse()                              { return course; }
    public void             setCourse(Course course)                 { this.course = course; }
    public AdmissionStatus  getAdmissionStatus()                     { return admissionStatus; }
    public void             setAdmissionStatus(AdmissionStatus s)    { this.admissionStatus = s; }
    public String           getDocumentPath()                        { return documentPath; }
    public void             setDocumentPath(String documentPath)     { this.documentPath = documentPath; }
    public String           getPhone()                               { return phone; }
    public void             setPhone(String phone)                   { this.phone = phone; }
    public String           getAddress()                             { return address; }
    public void             setAddress(String address)               { this.address = address; }
    public LocalDate        getDateOfBirth()                         { return dateOfBirth; }
    public void             setDateOfBirth(LocalDate dateOfBirth)    { this.dateOfBirth = dateOfBirth; }
    public String           getGender()                              { return gender; }
    public void             setGender(String gender)                 { this.gender = gender; }
    public String           getGuardianName()                        { return guardianName; }
    public void             setGuardianName(String guardianName)     { this.guardianName = guardianName; }
    public String           getGuardianPhone()                       { return guardianPhone; }
    public void             setGuardianPhone(String guardianPhone)   { this.guardianPhone = guardianPhone; }
    public String           getPreviousCollege()                     { return previousCollege; }
    public void             setPreviousCollege(String v)             { this.previousCollege = v; }
    public Double           getPreviousPercentage()                  { return previousPercentage; }
    public void             setPreviousPercentage(Double v)          { this.previousPercentage = v; }
    public String           getRollNumber()                          { return rollNumber; }
    public void             setRollNumber(String rollNumber)         { this.rollNumber = rollNumber; }
}
