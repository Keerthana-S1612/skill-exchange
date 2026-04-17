package com.example.skillexchange.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skill_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Student sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Student receiver;

    private String skillName;
    private String status; // PENDING, ACCEPTED, REJECTED
}