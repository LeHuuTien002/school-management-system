package com.tien.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;
    private String classId;
    private String subjectId;
    private LocalDateTime startTime;
    private int duration;

    public Schedule(String classId, String subjectId, LocalDateTime startTime, int duration) {
        this.classId = classId;
        this.subjectId = subjectId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    @Override
    public String toString() {
        return String.format("| %-6s | %-6s | %-19s | %3d |", classId, subjectId,
                startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), duration);
    }
}
