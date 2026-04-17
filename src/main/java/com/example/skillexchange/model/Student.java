package com.example.skillexchange.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String name;
    private String email;
    private String password;
    private String department;
    private String year;
    private String skillsKnown;
    private String skillsWanted;

    // 🏆 Innovation 1: Badge System
    private String badge;

    // ⭐ Innovation 2: Rating System
    private double rating;

    // ⏰ Innovation 3: Availability Status
    private String availability;
}