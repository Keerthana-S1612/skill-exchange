package com.example.skillexchange.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Student teacher;

    @ManyToOne
    @JoinColumn(name = "learner_id")
    private Student learner;

    private LocalDate date;
    private LocalTime time;
    private String mode; // Online / Offline
    private String meetingLink;
    private String status; // SCHEDULED, COMPLETED, CANCELLED

    private Integer rating;
    private String feedback;
}