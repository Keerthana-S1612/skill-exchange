package com.example.skillexchange.repository;

import com.example.skillexchange.model.SkillRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SkillRequestRepository extends JpaRepository<SkillRequest, Long> {
    List<SkillRequest> findByReceiver_StudentId(Long receiverId);
    List<SkillRequest> findBySender_StudentId(Long senderId);
}