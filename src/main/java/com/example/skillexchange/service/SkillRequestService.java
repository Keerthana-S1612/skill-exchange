package com.example.skillexchange.service;

import com.example.skillexchange.model.SkillRequest;
import com.example.skillexchange.repository.SkillRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SkillRequestService {

    @Autowired
    private SkillRequestRepository skillRequestRepository;

    public SkillRequest sendRequest(SkillRequest request) {
        request.setStatus("PENDING");
        return skillRequestRepository.save(request);
    }

    public SkillRequest updateStatus(Long requestId, String status) {
        SkillRequest request = skillRequestRepository.findById(requestId).orElseThrow();
        request.setStatus(status);
        return skillRequestRepository.save(request);
    }

    public List<SkillRequest> getRequestsForReceiver(Long receiverId) {
        return skillRequestRepository.findByReceiver_StudentId(receiverId);
    }

    public List<SkillRequest> getRequestsBySender(Long senderId) {
        return skillRequestRepository.findBySender_StudentId(senderId);
    }
}