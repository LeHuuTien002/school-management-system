package com.tien.db;

import com.tien.controller.SchoolController;
import com.tien.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class InMemoryDB implements Serializable {
    private static final long serialVersionUID = 1L;
    private TreeMap<String, User> users = new TreeMap<>();
    private HashMap<String, Student> students = new HashMap<>();
    private HashMap<String, Subject> subjects = new HashMap<>();
    private HashMap<String, String> classes = new HashMap<>();
    private List<Grade> grades = new ArrayList<>();
    private List<Schedule> schedules = new ArrayList<>();
    private List<Tuition> tuitions = new ArrayList<>();
    private List<Attendance> attendances = new ArrayList<>();

    public void sync(SchoolController sms) {
        sms.users = users;
        sms.students = students;
        sms.subjects = subjects;
        sms.classes = classes;
        sms.grades = grades;
        sms.schedules = schedules;
        sms.tuitions = tuitions;
        sms.attendances = attendances;
    }
    
    public TreeMap<String, User> getUsers() { return users; }
    public HashMap<String, Student> getStudents() { return students; }
    public HashMap<String, Subject> getSubjects() { return subjects; }
    public HashMap<String, String> getClasses() { return classes; }
    public List<Grade> getGrades() { return grades; }
    public List<Schedule> getSchedules() { return schedules; }
    public List<Tuition> getTuitions() { return tuitions; }
    public List<Attendance> getAttendances() { return attendances; }
}
