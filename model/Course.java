package com.college.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String  courseName;
    private Double  fees;
    private String  duration;
    private String  description;
    private Integer totalSeats;
    private Integer availableSeats;
    private boolean active = true;

    // ── no-arg constructor required by JPA ─────────
    public Course() {}

    private Course(Builder b) {
        this.courseName     = b.courseName;
        this.fees           = b.fees;
        this.duration       = b.duration;
        this.description    = b.description;
        this.totalSeats     = b.totalSeats;
        this.availableSeats = b.availableSeats;
        this.active         = b.active;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String  courseName;
        private Double  fees;
        private String  duration;
        private String  description;
        private Integer totalSeats;
        private Integer availableSeats;
        private boolean active = true;

        public Builder courseName(String v)     { this.courseName = v;     return this; }
        public Builder fees(Double v)           { this.fees = v;           return this; }
        public Builder duration(String v)       { this.duration = v;       return this; }
        public Builder description(String v)    { this.description = v;    return this; }
        public Builder totalSeats(Integer v)    { this.totalSeats = v;     return this; }
        public Builder availableSeats(Integer v){ this.availableSeats = v; return this; }
        public Builder active(boolean v)        { this.active = v;         return this; }
        public Course build()                   { return new Course(this); }
    }

    // ── getters / setters ───────────────────────────
    public Long    getId()                         { return id; }
    public void    setId(Long id)                  { this.id = id; }
    public String  getCourseName()                 { return courseName; }
    public void    setCourseName(String courseName){ this.courseName = courseName; }
    public Double  getFees()                       { return fees; }
    public void    setFees(Double fees)            { this.fees = fees; }
    public String  getDuration()                   { return duration; }
    public void    setDuration(String duration)    { this.duration = duration; }
    public String  getDescription()                { return description; }
    public void    setDescription(String d)        { this.description = d; }
    public Integer getTotalSeats()                 { return totalSeats; }
    public void    setTotalSeats(Integer v)        { this.totalSeats = v; }
    public Integer getAvailableSeats()             { return availableSeats; }
    public void    setAvailableSeats(Integer v)    { this.availableSeats = v; }
    public boolean isActive()                      { return active; }
    public void    setActive(boolean active)       { this.active = active; }
}
