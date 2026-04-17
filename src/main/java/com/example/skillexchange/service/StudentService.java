package com.example.skillexchange.service;

import com.example.skillexchange.model.Student;
import com.example.skillexchange.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student registerStudent(Student student) {
        // 🏆 Badge default
        student.setBadge("Beginner");
        // ⭐ Rating default
        student.setRating(0.0);
        // ⏰ Availability default
        student.setAvailability("Available");
        return studentRepository.save(student);
    }

    public Optional<Student> loginStudent(String email, String password) {
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent() && student.get().getPassword().equals(password)) {
            return student;
        }
        return Optional.empty();
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // ⏰ Update Availability
    public Student updateAvailability(Long id, String availability) {
        Student student = studentRepository.findById(id).orElseThrow();
        student.setAvailability(availability);
        return studentRepository.save(student);
    }

    // ⭐ Update Rating
    public Student updateRating(Long id, double rating) {
        Student student = studentRepository.findById(id).orElseThrow();
        student.setRating(rating);
        return studentRepository.save(student);
    }

    // 🏆 Update Badge based on activity
    public Student updateBadge(Long id, int skillsPosted, int sessionsCompleted) {
        Student student = studentRepository.findById(id).orElseThrow();
        if (sessionsCompleted >= 5) {
            student.setBadge("Top Mentor");
        } else if (skillsPosted >= 3) {
            student.setBadge("Active Learner");
        } else {
            student.setBadge("Beginner");
        }
        return studentRepository.save(student);
    }
}