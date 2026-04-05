package com.college.model;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "online_classes")
public class OnlineClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String meetingLink;
    private LocalDateTime scheduledAt;
    private String platform;   // Google Meet, Zoom, Teams etc.
    private String status;     // SCHEDULED, LIVE, COMPLETED, CANCELLED

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff staff;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public OnlineClass() {}

    // ── getters / setters ──
    public Long          getId()                          { return id; }
    public void          setId(Long id)                   { this.id = id; }
    public String        getTitle()                       { return title; }
    public void          setTitle(String title)           { this.title = title; }
    public String        getDescription()                 { return description; }
    public void          setDescription(String desc)      { this.description = desc; }
    public String        getMeetingLink()                 { return meetingLink; }
    public void          setMeetingLink(String link)      { this.meetingLink = link; }
    public LocalDateTime getScheduledAt()                 { return scheduledAt; }
    public void          setScheduledAt(LocalDateTime dt) { this.scheduledAt = dt; }
    public String        getPlatform()                    { return platform; }
    public void          setPlatform(String platform)     { this.platform = platform; }
    public String        getStatus()                      { return status; }
    public void          setStatus(String status)         { this.status = status; }
    public Course        getCourse()                      { return course; }
    public void          setCourse(Course course)         { this.course = course; }
    public Staff         getStaff()                       { return staff; }
    public void          setStaff(Staff staff)            { this.staff = staff; }
    public LocalDateTime getCreatedAt()                   { return createdAt; }
    public void          setCreatedAt(LocalDateTime dt)   { this.createdAt = dt; }
}