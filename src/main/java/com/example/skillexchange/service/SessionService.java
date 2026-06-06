package com.example.skillexchange.service;

import com.example.skillexchange.model.Session;
import com.example.skillexchange.model.Student;
import com.example.skillexchange.repository.SessionRepository;
import com.example.skillexchange.repository.StudentRepository;
import com.example.skillexchange.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SkillRepository skillRepository;

    public Session bookSession(Session session) {
        session.setStatus("SCHEDULED");
        return sessionRepository.save(session);
    }

    public Session addFeedback(Long sessionId, Integer rating, String feedback) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        session.setRating(rating);
        session.setFeedback(feedback);
        session.setStatus("COMPLETED");
        Session savedSession = sessionRepository.save(session);

        // Recalculate teacher's average rating and badge
        Student teacher = session.getTeacher();
        if (teacher != null) {
            List<Session> completedSessions = sessionRepository.findByTeacher_StudentId(teacher.getStudentId())
                    .stream()
                    .filter(s -> "COMPLETED".equals(s.getStatus()) && s.getRating() != null)
                    .toList();

            double avgRating = completedSessions.stream()
                    .mapToInt(Session::getRating)
                    .average()
                    .orElse(0.0);

            // Round to 1 decimal place
            avgRating = Math.round(avgRating * 10.0) / 10.0;
            teacher.setRating(avgRating);

            // Update badge based on activity
            int sessionsCompleted = completedSessions.size();
            int skillsPosted = skillRepository.findByStudent_StudentId(teacher.getStudentId()).size();

            if (sessionsCompleted >= 5) {
                teacher.setBadge("Top Mentor");
            } else if (skillsPosted >= 3) {
                teacher.setBadge("Active Learner");
            } else {
                teacher.setBadge("Beginner");
            }

            studentRepository.save(teacher);
        }

        return savedSession;
    }

    public List<Session> getSessionsByTeacher(Long teacherId) {
        return sessionRepository.findByTeacher_StudentId(teacherId);
    }

    public List<Session> getSessionsByLearner(Long learnerId) {
        return sessionRepository.findByLearner_StudentId(learnerId);
    }
}