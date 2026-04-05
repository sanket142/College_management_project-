package com.college.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.college.model.Fees;
import com.college.model.Student;
import com.college.service.AttendanceService;
import com.college.service.CourseService;
import com.college.service.FeesService;
import com.college.service.StudentService;
import com.college.service.OnlineClassService;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService    studentService;
    private final FeesService       feesService;
    private final CourseService     courseService;
    private final AttendanceService attendanceService;
    private final OnlineClassService onlineClassService;

    @Autowired
    public StudentController(StudentService studentService,
                             FeesService feesService,
                             CourseService courseService,
                             AttendanceService attendanceService, OnlineClassService onlineClassService) {
        this.studentService    = studentService;
        this.feesService       = feesService;
        this.courseService     = courseService;
        this.attendanceService = attendanceService;
        this.onlineClassService = onlineClassService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model) {
        studentService.findByEmail(principal.getName()).ifPresent(s -> {
            model.addAttribute("student",    s);
            model.addAttribute("fees",       feesService.findByStudent(principal.getName()));
            model.addAttribute("attendance", attendanceService.getAttendancePercentage(s.getId()));
        });
        return "student/dashboard";
    }

    @GetMapping("/apply")
    public String applyPage(Principal principal, Model model) {
        Student s = studentService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        if (s.getCourse() != null) return "redirect:/student/dashboard";
        model.addAttribute("courses", courseService.findActive());
        model.addAttribute("student", new Student());
        return "student/apply";
    }

    @PostMapping("/apply")
    public String submitApplication(@RequestParam Long courseId,
                                    @ModelAttribute Student student,
                                    Principal principal,
                                    RedirectAttributes ra) {
        try {
            studentService.applyAdmission(principal.getName(), courseId, student);
            ra.addFlashAttribute("success", "Application submitted! Waiting for admin approval.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/dashboard";
    }

    @GetMapping("/fees")
    public String feesPage(Principal principal, Model model) {
        List<Fees> feesList = feesService.findByStudent(principal.getName());

        int totalCount  = feesList.size();
        int paidCount   = 0;
        int unpaidCount = 0;
        for (Fees f : feesList) {
            if (f.getStatus() == Fees.PaymentStatus.PAID)   paidCount++;
            if (f.getStatus() == Fees.PaymentStatus.UNPAID) unpaidCount++;
        }

        model.addAttribute("feesList",    feesList);
        model.addAttribute("totalCount",  totalCount);
        model.addAttribute("paidCount",   paidCount);
        model.addAttribute("unpaidCount", unpaidCount);
        return "student/fees";
    }

    @PostMapping("/fees/pay/{id}")
    public String payFees(@PathVariable Long id,
                          @RequestParam(defaultValue = "BHIM UPI") String paymentMode,
                          @RequestParam(required = false) String utrNumber,
                          Principal principal,
                          RedirectAttributes ra) {
        try {
            Student student = studentService.findByEmail(principal.getName())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            List<Fees> myFees = feesService.findByStudentId(student.getId());
            boolean owns = false;
            for (Fees f : myFees) {
                if (f.getId().equals(id)) { owns = true; break; }
            }
            if (!owns) {
                ra.addFlashAttribute("error", "Unauthorized payment attempt.");
                return "redirect:/student/fees";
            }

            Fees paid = feesService.payFees(id, paymentMode, utrNumber);
            ra.addFlashAttribute("success",
                "Payment of Rs." + paid.getAmount() + " via BHIM UPI successful! " +
                "Transaction ID: " + paid.getTransactionId());

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Payment failed: " + e.getMessage());
        }
        return "redirect:/student/fees";
    }

    @GetMapping("/profile")
    public String profilePage(Principal principal, Model model) {
        studentService.findByEmail(principal.getName())
                .ifPresent(s -> model.addAttribute("student", s));
        return "student/profile";
    }

    @PostMapping("/upload-document")
    public String uploadDocument(@RequestParam MultipartFile file,
                                 Principal principal,
                                 RedirectAttributes ra) {
        try {
            studentService.uploadDocument(principal.getName(), file);
            ra.addFlashAttribute("success", "Document uploaded successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }
        return "redirect:/student/profile";
    }
    @GetMapping("/my-classes")
    public String myClasses(Principal principal, Model model) {
        studentService.findByEmail(principal.getName()).ifPresent(s -> {
            if (s.getCourse() != null) {
                model.addAttribute("classes",
                    onlineClassService.findByCourse(s.getCourse().getId()));
            }
        });
        return "student/my-classes";
    }
}
