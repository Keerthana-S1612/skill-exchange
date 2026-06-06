package com.example.skillexchange.service;

import com.example.skillexchange.model.Session;
import com.example.skillexchange.model.SkillRequest;
import com.example.skillexchange.repository.SessionRepository;
import com.example.skillexchange.repository.SkillRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class SkillRequestService {

    @Autowired
    private SkillRequestRepository skillRequestRepository;

    @Autowired
    private SessionRepository sessionRepository;

    public SkillRequest sendRequest(SkillRequest request) {
        request.setStatus("PENDING");
        return skillRequestRepository.save(request);
    }

    public SkillRequest updateStatus(Long requestId, String status) {
        SkillRequest request = skillRequestRepository.findById(requestId).orElseThrow();
        request.setStatus(status);
        SkillRequest savedRequest = skillRequestRepository.save(request);

        if ("ACCEPTED".equalsIgnoreCase(status)) {
            Session session = new Session();
            session.setTeacher(request.getReceiver()); // The receiver of the request is the teacher
            session.setLearner(request.getSender());   // The sender of the request is the learner
            session.setDate(LocalDate.now().plusDays(1)); // Default to tomorrow
            session.setTime(LocalTime.of(10, 0));       // Default to 10:00 AM
            session.setMode("Online");
            session.setMeetingLink("https://meet.google.com/mock-meet-link-" + requestId);
            session.setStatus("SCHEDULED");
            sessionRepository.save(session);
        }

        return savedRequest;
    }

    public List<SkillRequest> getRequestsForReceiver(Long receiverId) {
        return skillRequestRepository.findByReceiver_StudentId(receiverId);
    }

    public List<SkillRequest> getRequestsBySender(Long senderId) {
        return skillRequestRepository.findBySender_StudentId(senderId);
    }
}