package com.tien.model;

import com.tien.enums.UserRole;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private UserRole role;
    private List<String> notifications;

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.notifications = new ArrayList<String>();
    }

    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public boolean checkPassword(String password) {
        return password.equals(this.password);
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public void addNotification(String message) {
        notifications.add(LocalDateTime.now() + ": " + message);
    }
}
