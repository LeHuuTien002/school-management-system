package com.tien.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    private String eventId;
    private String name;
    private LocalDateTime startTime;
    private String description;

    public Event(String eventId, String name, LocalDateTime startTime, String description) {
        this.eventId = eventId;
        this.name = name;
        this.startTime = startTime;
        this.description = description;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return String.format("| %-8s | %-20s | %-19s | %-30s |", eventId, name, startTime, description);
    }
}
