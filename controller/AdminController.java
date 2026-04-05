package com.college.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.college.model.Course;
import com.college.model.Student;
import com.college.service.CourseService;
import com.college.service.FeesService;
import com.college.service.StaffService;
import com.college.service.StudentService;
import com.college.service.UserService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.college.dto.RegisterDTO;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final StudentService studentService;
    private final UserService    userService;
    private final CourseService  courseService;
    private final FeesService    feesService;
    private final StaffService staffService;

    @Autowired
    public AdminController(StudentService studentService,
                           UserService userService,
                           CourseService courseService,
                           FeesService feesService,
                           StaffService staffService) {
        this.studentService = studentService;
        this.userService    = userService;
        this.courseService  = courseService;
        this.feesService    = feesService;
        this.staffService   = staffService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalStudents",    studentService.findAll().size());
        model.addAttribute("pendingAdmissions",studentService.countByStatus(Student.AdmissionStatus.PENDING));
        model.addAttribute("approvedStudents", studentService.countByStatus(Student.AdmissionStatus.APPROVED));
        model.addAttribute("totalCourses",     courseService.findAll().size());
        model.addAttribute("totalCollected",   feesService.totalCollected());
        model.addAttribute("totalPending",     feesService.totalPending());
        model.addAttribute("recentStudents",   studentService.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }

    @GetMapping("/students")
    public String students(@RequestParam(required = false) String search,
                           @RequestParam(required = false) String status,
                           Model model) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("students", studentService.search(search));
        } else if (status != null && !status.isBlank()) {
            model.addAttribute("students", studentService.findByStatus(Student.AdmissionStatus.valueOf(status)));
        } else {
            model.addAttribute("students", studentService.findAll());
        }
        model.addAttribute("search", search);
        model.addAttribute("selectedStatus", status);
        return "admin/students";
    }

    @GetMapping("/students/{id}")
    public String studentDetail(@PathVariable Long id, Model model) {
        studentService.findById(id).ifPresent(s -> {
            model.addAttribute("student", s);
            model.addAttribute("fees", feesService.findByStudentId(id));
        });
        return "admin/student-detail";
    }

    @PostMapping("/students/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               RedirectAttributes ra) {
        try {
            studentService.updateStatus(id, Student.AdmissionStatus.valueOf(status));
            ra.addFlashAttribute("success", "Status updated to " + status);
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/students/" + id;
    }

    @GetMapping("/courses")
    public String courses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("course",  new Course());
        return "admin/courses";
    }

    @PostMapping("/courses/save")
    public String saveCourse(@ModelAttribute Course course, RedirectAttributes ra) {
        courseService.save(course);
        ra.addFlashAttribute("success", "Course saved!");
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes ra) {
        courseService.delete(id);
        ra.addFlashAttribute("success", "Course deleted!");
        return "redirect:/admin/courses";
    }

    @GetMapping("/fees")
    public String fees(Model model) {
        model.addAttribute("allFees",       feesService.findAll());
        model.addAttribute("totalCollected",feesService.totalCollected());
        model.addAttribute("totalPending",  feesService.totalPending());
        return "admin/fees";
    }

    @PostMapping("/fees/add")
    public String addFees(@RequestParam Long studentId,
                          @RequestParam Double amount,
                          @RequestParam String semester,
                          @RequestParam String dueDate,
                          RedirectAttributes ra) {
        feesService.createFeeRecord(studentId, amount, semester, LocalDate.parse(dueDate));
        ra.addFlashAttribute("success", "Fee record created!");
        return "redirect:/admin/fees";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        userService.deleteUser(id);
        ra.addFlashAttribute("success", "User deleted!");
        return "redirect:/admin/users";
    }
    @GetMapping("/documents")
    public String viewDocuments(Model model) {
        // Get all students who have uploaded a document
        List<com.college.model.Student> students = studentService.findAll()
                .stream()
                .filter(s -> s.getDocumentPath() != null)
                .toList();
        model.addAttribute("students", students);
        return "admin/documents";
    }

    @GetMapping("/documents/download/{studentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long studentId) {
        try {
            com.college.model.Student student = studentService.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            Path filePath = Paths.get("uploads/documents/")
                    .resolve(student.getDocumentPath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + student.getDocumentPath() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/staff")
    public String staffPage(Model model) {
        model.addAttribute("staffList", staffService.findAll());
        model.addAttribute("dto", new RegisterDTO());
        return "admin/staff";
    }

    @PostMapping("/staff/add")
    public String addStaff(@ModelAttribute RegisterDTO dto,
                            @RequestParam String department,
                            @RequestParam String designation,
                            @RequestParam String employeeId,
                            @RequestParam String phone,
                            RedirectAttributes ra) {
        try {
            userService.registerStaffFull(dto, department, designation, employeeId, phone);
            ra.addFlashAttribute("success",
                "Staff account created! Login: " + dto.getEmail() + " / " + dto.getPassword());
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/staff";
    }

    @PostMapping("/staff/delete/{id}")
    public String deleteStaff(@PathVariable Long id, RedirectAttributes ra) {
        staffService.delete(id);
        ra.addFlashAttribute("success", "Staff member deleted.");
        return "redirect:/admin/staff";
    }
    
}
