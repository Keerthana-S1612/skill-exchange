package com.example.skillexchange.service;

import com.example.skillexchange.model.Session;
import com.example.skillexchange.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public Session bookSession(Session session) {
        session.setStatus("SCHEDULED");
        return sessionRepository.save(session);
    }

    public Session addFeedback(Long sessionId, Integer rating, String feedback) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        session.setRating(rating);
        session.setFeedback(feedback);
        session.setStatus("COMPLETED");
        return sessionRepository.save(session);
    }

    public List<Session> getSessionsByTeacher(Long teacherId) {
        return sessionRepository.findByTeacher_StudentId(teacherId);
    }

    public List<Session> getSessionsByLearner(Long learnerId) {
        return sessionRepository.findByLearner_StudentId(learnerId);
    }
}