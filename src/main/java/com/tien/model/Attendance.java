package com.tien.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Attendance implements Serializable {
    private static final long serialVersionUID = 1L;
    private String studentId;
    private String scheduleId;
    private LocalDateTime checkInTime;
    private boolean present;

    public Attendance(String studentId, String scheduleId, LocalDateTime checkInTime, boolean present) {
        this.studentId = studentId;
        this.scheduleId = scheduleId;
        this.checkInTime = checkInTime;
        this.present = present;
    }

    public String getStudentId() { return studentId; }
    public boolean isPresent() { return present; }
    @Override
    public String toString() {
        return String.format("| %-8s | %-19s | %-5s |", studentId, checkInTime, present ? "Yes" : "No");
    }
}
