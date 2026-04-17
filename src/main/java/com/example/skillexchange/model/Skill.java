package com.example.skillexchange.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    private String skillName;
    private String description;
    private String availableTime;
    private String skillLevel; // Beginner, Intermediate, Expert

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
}