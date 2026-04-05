package com.college.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.college.model.Attendance;
import com.college.repository.AttendanceRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<Attendance> findByStudentId(Long studentId) {
        return attendanceRepository.findByStudent_Id(studentId);
    }

    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public double getAttendancePercentage(Long studentId) {
        long total   = attendanceRepository.countTotal(studentId);
        if (total == 0) return 0.0;
        long present = attendanceRepository.countPresent(studentId);
        return (present * 100.0) / total;
    }
}
