package com.example.skillexchange.controller;

import com.example.skillexchange.model.Student;
import com.example.skillexchange.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/register")
    public ResponseEntity<Student> register(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.registerStudent(student));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String password) {
        return studentService.loginStudent(email, password)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build());
    }

    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ⏰ Update Availability
    @PutMapping("/{id}/availability")
    public ResponseEntity<Student> updateAvailability(@PathVariable Long id,
                                                      @RequestParam String availability) {
        return ResponseEntity.ok(studentService.updateAvailability(id, availability));
    }

    // ⭐ Update Rating
    @PutMapping("/{id}/rating")
    public ResponseEntity<Student> updateRating(@PathVariable Long id,
                                                @RequestParam double rating) {
        return ResponseEntity.ok(studentService.updateRating(id, rating));
    }

    // 🏆 Update Badge
    @PutMapping("/{id}/badge")
    public ResponseEntity<Student> updateBadge(@PathVariable Long id,
                                               @RequestParam int skillsPosted,
                                               @RequestParam int sessionsCompleted) {
        return ResponseEntity.ok(studentService.updateBadge(id, skillsPosted, sessionsCompleted));
    }
}