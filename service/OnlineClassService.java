package com.college.service;

import com.college.model.*;
import com.college.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OnlineClassService {

    private final OnlineClassRepository classRepository;
    private final StaffRepository       staffRepository;
    private final CourseRepository      courseRepository;

    @Autowired
    public OnlineClassService(OnlineClassRepository classRepository,
                              StaffRepository staffRepository,
                              CourseRepository courseRepository) {
        this.classRepository  = classRepository;
        this.staffRepository  = staffRepository;
        this.courseRepository = courseRepository;
    }

    public List<OnlineClass> findAll()                    { return classRepository.findByOrderByScheduledAtDesc(); }
    public List<OnlineClass> findByCourse(Long courseId)  { return classRepository.findByCourse_Id(courseId); }
    public List<OnlineClass> findLive()                   { return classRepository.findByStatus("LIVE"); }
    public Optional<OnlineClass> findById(Long id)        { return classRepository.findById(id); }

    @Transactional
    public OnlineClass schedule(String staffEmail, Long courseId,
                                String title, String description,
                                String meetingLink, String platform,
                                String scheduledAt) {
        Staff staff = staffRepository.findByUser_Email(staffEmail)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        OnlineClass oc = new OnlineClass();
        oc.setTitle(title);
        oc.setDescription(description);
        oc.setMeetingLink(meetingLink);
        oc.setPlatform(platform);
        oc.setScheduledAt(java.time.LocalDateTime.parse(scheduledAt));
        oc.setStatus("SCHEDULED");
        oc.setCourse(course);
        oc.setStaff(staff);
        return classRepository.save(oc);
    }

    @Transactional
    public void updateStatus(Long id, String status) {
        classRepository.findById(id).ifPresent(oc -> {
            oc.setStatus(status);
            classRepository.save(oc);
        });
    }

    @Transactional
    public void delete(Long id) { classRepository.deleteById(id); }
}