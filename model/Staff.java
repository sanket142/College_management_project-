package com.college.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String department;
    private String designation;
    private String employeeId;
    private String phone;

    // ── no-arg constructor required by JPA ─────────
    public Staff() {}

    private Staff(Builder b) {
        this.user        = b.user;
        this.department  = b.department;
        this.designation = b.designation;
        this.employeeId  = b.employeeId;
        this.phone       = b.phone;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private User   user;
        private String department;
        private String designation;
        private String employeeId;
        private String phone;

        public Builder user(User v)           { this.user        = v; return this; }
        public Builder department(String v)   { this.department  = v; return this; }
        public Builder designation(String v)  { this.designation = v; return this; }
        public Builder employeeId(String v)   { this.employeeId  = v; return this; }
        public Builder phone(String v)        { this.phone       = v; return this; }
        public Staff build()                  { return new Staff(this); }
    }

    // ── getters / setters ───────────────────────────
    public Long   getId()                        { return id; }
    public void   setId(Long id)                 { this.id = id; }
    public User   getUser()                      { return user; }
    public void   setUser(User user)             { this.user = user; }
    public String getDepartment()                { return department; }
    public void   setDepartment(String dept)     { this.department = dept; }
    public String getDesignation()               { return designation; }
    public void   setDesignation(String desig)   { this.designation = desig; }
    public String getEmployeeId()                { return employeeId; }
    public void   setEmployeeId(String empId)    { this.employeeId = empId; }
    public String getPhone()                     { return phone; }
    public void   setPhone(String phone)         { this.phone = phone; }
}
