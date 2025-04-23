package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class Mark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private int score;

    private String status; 

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public Mark() {}

    public Mark(String subject, int score, Student student) {
        this.subject = subject;
        this.score = score;
        this.student = student;
        this.status = score >= 30 ? "Pass" : "Fail"; 
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}


