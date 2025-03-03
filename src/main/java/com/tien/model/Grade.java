package com.tien.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Grade implements Serializable {
    public static final long serialVersionUID = 1L;
    private String studentId;
    private String subjectId;
    private double score;
    private LocalDate date;

    public Grade(String studentId, String subjectId, double score, LocalDate date) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.score = score;
        this.date = date;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public double getScore() {
        return score;
    }
    
    public LocalDate getDate() {
        return date;
    }
}
