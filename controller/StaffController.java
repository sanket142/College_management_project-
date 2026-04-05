package com.college.controller;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.college.model.Attendance;
import com.college.model.Course;
import com.college.model.Student;
import com.college.service.AttendanceService;
import com.college.service.CourseService;
import com.college.service.FeesService;
import com.college.service.StudentService;
import com.college.service.OnlineClassService;



@Controller
@RequestMapping("/staff")
public class StaffController {

	private final OnlineClassService onlineClassService;
    private final StudentService    studentService;
    private final CourseService     courseService;
    private final AttendanceService attendanceService;
    private final FeesService       feesService;
    

    @Autowired
    public StaffController(StudentService studentService,
                           CourseService courseService,
                           AttendanceService attendanceService,
                           FeesService feesService,OnlineClassService onlineClassService) {
        this.onlineClassService = onlineClassService;
		this.studentService    = studentService;
        this.courseService     = courseService;
        this.attendanceService = attendanceService;
        this.feesService       = feesService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalStudents",    studentService.findAll().size());
        model.addAttribute("approvedStudents", studentService.countByStatus(Student.AdmissionStatus.APPROVED));
        model.addAttribute("courses",          courseService.findAll());
        model.addAttribute("recentStudents",   studentService.findAll().stream().limit(6).toList());
        return "staff/dashboard";
    }

    @GetMapping("/students")
    public String students(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("students", studentService.search(search));
        } else {
            model.addAttribute("students", studentService.findByStatus(Student.AdmissionStatus.APPROVED));
        }
        model.addAttribute("search", search);
        return "staff/students";
    }

    @GetMapping("/attendance")
    public String attendance(Model model) {
        model.addAttribute("students", studentService.findByStatus(Student.AdmissionStatus.APPROVED));
        model.addAttribute("courses",  courseService.findAll());
        return "staff/attendance";
    }

    @PostMapping("/attendance/mark")
    public String markAttendance(@RequestParam Long studentId,
                                 @RequestParam Long courseId,
                                 @RequestParam String status,
                                 @RequestParam(required = false) String remarks,
                                 RedirectAttributes ra) {
        try {
            Student student = studentService.findById(studentId).orElseThrow();
            Course  course  = courseService.findById(courseId).orElseThrow();
            Attendance att = Attendance.builder()
                    .student(student)
                    .course(course)
                    .date(LocalDate.now())
                    .status(Attendance.AttendanceStatus.valueOf(status))
                    .remarks(remarks)
                    .build();
            attendanceService.save(att);
            ra.addFlashAttribute("success", "Attendance marked!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/staff/attendance";
    }

    @GetMapping("/fees")
    public String feeStatus(Model model) {
        model.addAttribute("allFees",       feesService.findAll());
        model.addAttribute("totalCollected",feesService.totalCollected());
        model.addAttribute("totalPending",  feesService.totalPending());
        return "staff/fees";
    }
    @GetMapping("/online-classes")
    public String onlineClasses(Principal principal, Model model) {
        model.addAttribute("classes", onlineClassService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "staff/online-classes";
    }

    @PostMapping("/online-classes/schedule")
    public String scheduleClass(@RequestParam Long courseId,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam String meetingLink,
                                 @RequestParam String platform,
                                 @RequestParam String scheduledAt,
                                 Principal principal,
                                 RedirectAttributes ra) {
        try {
            onlineClassService.schedule(principal.getName(), courseId,
                    title, description, meetingLink, platform, scheduledAt);
            ra.addFlashAttribute("success", "Class scheduled successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/staff/online-classes";
    }	

    @PostMapping("/online-classes/{id}/status")
    public String updateClassStatus(@PathVariable Long id,
                                     @RequestParam String status,
                                     RedirectAttributes ra) {
        onlineClassService.updateStatus(id, status);
        ra.addFlashAttribute("success", "Class status updated to " + status);
        return "redirect:/staff/online-classes";
    }

    @PostMapping("/online-classes/{id}/delete")
    public String deleteClass(@PathVariable Long id, RedirectAttributes ra) {
        onlineClassService.delete(id);
        ra.addFlashAttribute("success", "Class deleted.");
        return "redirect:/staff/online-classes";
    }
}
