package com.tien.model;

import java.io.Serializable;

public class Subject implements Serializable {
    private static final long serialVersionUID = 1L;
    private String subjectId;
    private String subjectName;
    private int credits;

    public Subject(String subjectId, String subjectName, int credits) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.credits = credits;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public int getCredits() {
        return credits;
    }
    
    public String getSubjectName() {
        return subjectName;
    }

    @Override
    public String toString() {
        return String.format("| %-6s | %-20s | %2d |", subjectId, subjectName, credits);
    }
}
