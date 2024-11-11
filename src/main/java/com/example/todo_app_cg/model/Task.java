package com.example.todo_app_cg.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;
    private LocalDate dueDate;
    private String priority;
    private String category;
    private String status = "pending";


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
