package com.example.skillexchange.controller;

import com.example.skillexchange.model.Session;
import com.example.skillexchange.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @PostMapping("/book")
    public ResponseEntity<Session> bookSession(@RequestBody Session session) {
        return ResponseEntity.ok(sessionService.bookSession(session));
    }

    @PutMapping("/{id}/feedback")
    public ResponseEntity<Session> addFeedback(@PathVariable Long id,
                                               @RequestParam Integer rating,
                                               @RequestParam String feedback) {
        return ResponseEntity.ok(sessionService.addFeedback(id, rating, feedback));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<Session>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(sessionService.getSessionsByTeacher(teacherId));
    }

    @GetMapping("/learner/{learnerId}")
    public ResponseEntity<List<Session>> getByLearner(@PathVariable Long learnerId) {
        return ResponseEntity.ok(sessionService.getSessionsByLearner(learnerId));
    }
}