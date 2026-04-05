package com.college.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enabled = true;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum Role { ROLE_ADMIN, ROLE_STAFF, ROLE_STUDENT }

    // ── no-arg constructor required by JPA ─────────
    public User() {}

    private User(Builder b) {
        this.name     = b.name;
        this.email    = b.email;
        this.password = b.password;
        this.role     = b.role;
        this.enabled  = b.enabled;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String  name;
        private String  email;
        private String  password;
        private Role    role;
        private boolean enabled = true;

        public Builder name(String v)     { this.name     = v; return this; }
        public Builder email(String v)    { this.email    = v; return this; }
        public Builder password(String v) { this.password = v; return this; }
        public Builder role(Role v)       { this.role     = v; return this; }
        public Builder enabled(boolean v) { this.enabled  = v; return this; }
        public User build()               { return new User(this); }
    }

    // ── getters / setters ───────────────────────────
    public Long          getId()                       { return id; }
    public void          setId(Long id)                { this.id = id; }
    public String        getName()                     { return name; }
    public void          setName(String name)          { this.name = name; }
    public String        getEmail()                    { return email; }
    public void          setEmail(String email)        { this.email = email; }
    public String        getPassword()                 { return password; }
    public void          setPassword(String password)  { this.password = password; }
    public Role          getRole()                     { return role; }
    public void          setRole(Role role)            { this.role = role; }
    public boolean       isEnabled()                   { return enabled; }
    public void          setEnabled(boolean enabled)   { this.enabled = enabled; }
    public LocalDateTime getCreatedAt()                { return createdAt; }
    public void          setCreatedAt(LocalDateTime v) { this.createdAt = v; }
}
