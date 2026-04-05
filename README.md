# 🎓 College Admission & Maintenance System
**Spring Boot + Thymeleaf + MySQL + Spring Security**

---

## 🚀 Quick Start

### 1. Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.8+

### 2. Database Setup
```sql
CREATE DATABASE college_db;
```

### 3. Configure Database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/college_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 4. Run
```bash
mvn spring-boot:run
```

Open: **http://localhost:8080**

---

## 🔐 Default Login Credentials

| Role    | Email                | Password  |
|---------|----------------------|-----------|
| Admin   | admin@college.com    | admin123  |
| Student | Register via /auth/register | — |

To create a staff account, register as student then change role in DB:
```sql
UPDATE users SET role = 'ROLE_STAFF' WHERE email = 'staff@example.com';
INSERT INTO staff (user_id, department) SELECT id, 'Computer Science' FROM users WHERE email = 'staff@example.com';
```

---

## 📁 Project Structure

```
src/main/java/com/college/
├── CollegeApplication.java          ← Entry point
├── config/
│   ├── SecurityConfig.java          ← Spring Security, role routing
│   └── DataInitializer.java         ← Seed admin + courses
├── controller/
│   ├── AuthController.java          ← /auth/login, /auth/register
│   ├── StudentController.java       ← /student/**
│   ├── AdminController.java         ← /admin/**
│   └── StaffController.java         ← /staff/**
├── service/
│   ├── CustomUserDetailsService.java
│   ├── UserService.java
│   ├── StudentService.java
│   ├── FeesService.java
│   ├── CourseService.java
│   └── AttendanceService.java
├── repository/
│   ├── UserRepository.java
│   ├── StudentRepository.java
│   ├── FeesRepository.java
│   ├── CourseRepository.java
│   ├── StaffRepository.java
│   └── AttendanceRepository.java
├── model/
│   ├── User.java          ← Roles: ADMIN, STAFF, STUDENT
│   ├── Student.java
│   ├── Course.java
│   ├── Fees.java
│   ├── Staff.java
│   └── Attendance.java
└── dto/
    └── RegisterDTO.java

src/main/resources/
├── application.properties
├── static/css/style.css
└── templates/
    ├── auth/
    │   ├── login.html
    │   └── register.html
    ├── fragments/
    │   └── sidebar.html             ← Shared nav for all roles
    ├── admin/
    │   ├── dashboard.html
    │   ├── students.html
    │   ├── student-detail.html
    │   ├── courses.html
    │   ├── fees.html
    │   └── users.html
    ├── student/
    │   ├── dashboard.html
    │   ├── apply.html
    │   ├── fees.html
    │   └── profile.html
    └── staff/
        ├── dashboard.html
        ├── students.html
        ├── attendance.html
        └── fees.html
```

---

## 🧩 Module Summary

### 👨‍🎓 Student Module (`/student/**`)
| URL | Description |
|-----|-------------|
| `/student/dashboard` | Overview – status, fees, attendance |
| `/student/apply` | Apply for admission + course selection |
| `/student/fees` | View & pay fees with transaction ID |
| `/student/profile` | View profile + upload documents |
| `/student/upload-document` | POST – upload PDF/image |

### 🧑‍💼 Admin Module (`/admin/**`)
| URL | Description |
|-----|-------------|
| `/admin/dashboard` | Stats, recent students |
| `/admin/students` | List, search, filter students |
| `/admin/students/{id}` | Detail – update admission status |
| `/admin/courses` | Add / delete courses |
| `/admin/fees` | All fees, create fee records |
| `/admin/users` | All users, delete users |

### 🧑‍💻 Staff Module (`/staff/**`)
| URL | Description |
|-----|-------------|
| `/staff/dashboard` | Summary stats |
| `/staff/students` | View approved students |
| `/staff/attendance` | Mark attendance (present/absent/late) |
| `/staff/fees` | View fee status (read-only) |

---

## 🔐 Security

- Passwords hashed with **BCrypt**
- Role-based URL protection via `SecurityFilterChain`
- Login redirects to role-specific dashboard
- Session invalidated on logout

---

## 🗄️ Database Tables (auto-created by Hibernate)

| Table | Key Columns |
|-------|-------------|
| `users` | id, name, email, password, role, enabled |
| `students` | id, user_id, course_id, admission_status, document_path |
| `courses` | id, course_name, fees, duration, total_seats |
| `fees` | id, student_id, amount, semester, status, payment_date, transaction_id |
| `staff` | id, user_id, department, designation |
| `attendance` | id, student_id, course_id, date, status |

---

## 🔥 Advanced Features (Optional Add-ons)

### Email Notification on Approval
Add to `StudentService.updateStatus()`:
```java
// Inject JavaMailSender and send email when status == APPROVED
mailSender.send(message -> {
    message.setTo(student.getUser().getEmail());
    message.setSubject("Admission Approved – VidyaMandir College");
    message.setText("Congratulations! Your admission has been approved.");
});
```

### Razorpay Integration
Replace the pay button in `fees.html` with Razorpay checkout:
```html
<button id="rzp-button" class="btn btn-primary">Pay with Razorpay</button>
<script src="https://checkout.razorpay.com/v1/checkout.js"></script>
<script>
var options = {
  key: "YOUR_RAZORPAY_KEY",
  amount: /*[[${f.amount * 100}]]*/ 0,
  currency: "INR",
  name: "VidyaMandir College",
  handler: function(response) {
    // POST transactionId to /student/fees/confirm
  }
};
new Razorpay(options).open();
</script>
```

---

## ✅ Test Checklist

- [ ] Register new student → auto-creates Student record
- [ ] Login as student → redirected to `/student/dashboard`
- [ ] Apply for admission → status shows PENDING
- [ ] Login as admin → approve student
- [ ] Fee record auto-created on approval
- [ ] Student pays fee → PAID badge + Transaction ID shown
- [ ] Staff marks attendance → percentage updates
- [ ] Upload document → filename stored in DB
"# College_management_project-" 
