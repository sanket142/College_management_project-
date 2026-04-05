package com.college.repository;

import com.college.model.OnlineClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OnlineClassRepository extends JpaRepository<OnlineClass, Long> {
    List<OnlineClass> findByCourse_Id(Long courseId);
    List<OnlineClass> findByStatus(String status);
    List<OnlineClass> findByOrderByScheduledAtDesc();
}	