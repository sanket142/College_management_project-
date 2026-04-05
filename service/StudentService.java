package com.college.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.college.model.Course;
import com.college.model.Fees;
import com.college.model.Staff;
import com.college.model.Student;
import com.college.repository.CourseRepository;
import com.college.repository.FeesRepository;
import com.college.repository.StaffRepository;
import com.college.repository.StudentRepository;
import com.college.repository.UserRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository  courseRepository;
    private final FeesRepository    feesRepository;
    private final StaffRepository staffRepository;
    private final UserRepository  userRepository;
    
    @Autowired
    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          FeesRepository feesRepository,StaffRepository staffRepository,UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository  = courseRepository;
        this.feesRepository    = feesRepository;
        this.staffRepository = staffRepository;
        this.userRepository  = userRepository;
    }

    public Optional<Student> findByEmail(String email) { return studentRepository.findByUser_Email(email); }
    public Optional<Student> findById(Long id)         { return studentRepository.findById(id); }
    public List<Student>     findAll()                  { return studentRepository.findAll(); }
    public List<Student>     findByStatus(Student.AdmissionStatus s) { return studentRepository.findByAdmissionStatus(s); }
    public List<Student>     search(String q)           { return studentRepository.search(q); }
    public long              countByStatus(Student.AdmissionStatus s){ return studentRepository.countByAdmissionStatus(s); }
//    public List<Staff> 		 findAllstaff(){return staffRepository.findAll();}
    @Transactional
    public Student applyAdmission(String email, Long courseId, Student details) {
        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        student.setCourse(course);
        student.setPhone(details.getPhone());
        student.setAddress(details.getAddress());
        student.setDateOfBirth(details.getDateOfBirth());
        student.setGender(details.getGender());
        student.setGuardianName(details.getGuardianName());
        student.setGuardianPhone(details.getGuardianPhone());
        student.setPreviousCollege(details.getPreviousCollege());
        student.setPreviousPercentage(details.getPreviousPercentage());
        student.setAdmissionStatus(Student.AdmissionStatus.PENDING);
        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStatus(Long studentId, Student.AdmissionStatus status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setAdmissionStatus(status);
        if (status == Student.AdmissionStatus.APPROVED && student.getCourse() != null) {
            Fees fees = Fees.builder()
                    .student(student)
                    .amount(student.getCourse().getFees())
                    .semester("Semester 1")
                    .status(Fees.PaymentStatus.UNPAID)
                    .build();
            feesRepository.save(fees);
        }
        return studentRepository.save(student);
    }

    @Transactional
    public String uploadDocument(String email, MultipartFile file) throws IOException {
        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        String uploadDir = "uploads/documents/";
        Files.createDirectories(Paths.get(uploadDir));
        String filename = student.getId() + "_" + file.getOriginalFilename();
        Files.write(Paths.get(uploadDir + filename), file.getBytes());
        student.setDocumentPath(filename);
        studentRepository.save(student);
        return filename;
    }
//    public List<Staff> findAll() {
//        return StaffRepository.findAll();
//    }
//    @Transactional
//    public void delete(Long id) {
//        // First delete the staff record, then the user
//    	staffRepository.findById(id).ifPresent(s -> {
//            Long userId = s.getUser().getId();
//            staffRepository.deleteById(id);
//            userRepository.deleteById(userId);
//        });
//    }
}
