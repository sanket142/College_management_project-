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
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    private String remarks;

    public enum AttendanceStatus { PRESENT, ABSENT, LATE }

    // ── no-arg constructor required by JPA ─────────
    public Attendance() {}

    private Attendance(Builder b) {
        this.student = b.student;
        this.course  = b.course;
        this.date    = b.date;
        this.status  = b.status;
        this.remarks = b.remarks;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Student          student;
        private Course           course;
        private LocalDate        date;
        private AttendanceStatus status;
        private String           remarks;

        public Builder student(Student v)          { this.student = v; return this; }
        public Builder course(Course v)            { this.course  = v; return this; }
        public Builder date(LocalDate v)           { this.date    = v; return this; }
        public Builder status(AttendanceStatus v)  { this.status  = v; return this; }
        public Builder remarks(String v)           { this.remarks = v; return this; }
        public Attendance build()                  { return new Attendance(this); }
    }

    // ── getters / setters ───────────────────────────
    public Long             getId()                              { return id; }
    public void             setId(Long id)                       { this.id = id; }
    public Student          getStudent()                         { return student; }
    public void             setStudent(Student student)          { this.student = student; }
    public Course           getCourse()                          { return course; }
    public void             setCourse(Course course)             { this.course = course; }
    public LocalDate        getDate()                            { return date; }
    public void             setDate(LocalDate date)              { this.date = date; }
    public AttendanceStatus getStatus()                          { return status; }
    public void             setStatus(AttendanceStatus status)   { this.status = status; }
    public String           getRemarks()                         { return remarks; }
    public void             setRemarks(String remarks)           { this.remarks = remarks; }
}
