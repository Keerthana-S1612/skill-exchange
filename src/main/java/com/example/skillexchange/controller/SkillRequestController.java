package com.example.skillexchange.controller;

import com.example.skillexchange.model.SkillRequest;
import com.example.skillexchange.service.SkillRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class SkillRequestController {

    @Autowired
    private SkillRequestService skillRequestService;

    @PostMapping("/send")
    public ResponseEntity<SkillRequest> sendRequest(@RequestBody SkillRequest request) {
        return ResponseEntity.ok(skillRequestService.sendRequest(request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SkillRequest> updateStatus(@PathVariable Long id,
                                                     @RequestParam String status) {
        return ResponseEntity.ok(skillRequestService.updateStatus(id, status));
    }

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<SkillRequest>> getReceived(@PathVariable Long receiverId) {
        return ResponseEntity.ok(skillRequestService.getRequestsForReceiver(receiverId));
    }

    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<SkillRequest>> getSent(@PathVariable Long senderId) {
        return ResponseEntity.ok(skillRequestService.getRequestsBySender(senderId));
    }
}