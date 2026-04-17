package com.example.skillexchange.repository;

import com.example.skillexchange.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByTeacher_StudentId(Long teacherId);
    List<Session> findByLearner_StudentId(Long learnerId);
}