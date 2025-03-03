package com.tien.controller;

import com.tien.db.InMemoryDB;
import com.tien.enums.UserRole;
import com.tien.model.*;
import com.tien.view.ConsoleView;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SchoolController implements Serializable {
    private static final long serialVersionUID = 1L;
    public HashMap<String, User> users;
    public HashMap<String, Student> students;
    public HashMap<String, Subject> subjects;
    public HashMap<String, String> classes;
    public List<Grade> grades;
    public List<Schedule> schedules;
    public List<Tuition> tuitions;
    public List<Attendance> attendances;
    private User currentUser;
    private InMemoryDB db;
    private transient ConsoleView view;
    private static final String DATA_FILE = "school_data.dat";

    public SchoolController(ConsoleView view) {
        this.view = view;
        this.db = new InMemoryDB();
        loadData();
        initDefaultUser();
    }

    private void initializeEmptyData() {
        users = new HashMap<>();
        students = new HashMap<>();
        subjects = new HashMap<>();
        classes = new HashMap<>();
        grades = new ArrayList<>();
        schedules = new ArrayList<>();
        tuitions = new ArrayList<>();
        attendances = new ArrayList<>();
    }

    private void initDefaultUser() {
        if (!users.containsKey("admin")) {
            users.put("admin", new User("admin", "admin123", UserRole.ADMIN));
            view.displayMessage("Initialized default admin user.");
        }
    }

    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                SchoolController loaded = (SchoolController) ois.readObject();
                this.users = loaded.users;
                this.students = loaded.students;
                this.subjects = loaded.subjects;
                this.classes = loaded.classes;
                this.grades = loaded.grades;
                this.schedules = loaded.schedules;
                this.tuitions = loaded.tuitions;
                this.attendances = loaded.attendances;
                view.displayMessage("Data loaded successfully: " + users.size() + " users, " +
                        students.size() + " students. Users: " + users.keySet());
            } catch (IOException | ClassNotFoundException e) {
                view.displayMessage("Error loading data: " + e.getMessage());
                e.printStackTrace();
                initializeEmptyData();
            }
        } else {
            view.displayMessage("No data file found, starting fresh.");
            initializeEmptyData();
        }
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(this);
            view.displayMessage("Data saved successfully: " + users.size() + " users, " +
                    students.size() + " students to " + DATA_FILE);
        } catch (IOException e) {
            view.displayMessage("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.checkPassword(password)) {
            currentUser = user;
            view.displayMessage("Login successful! Welcome " + username + " (" + user.getRole() + ")");
            view.displayNotifications(user.getNotifications());
            return true;
        }
        view.displayMessage("Invalid credentials!");
        return false;
    }

    public void listUsers() {
        checkPermission(UserRole.ADMIN);
        view.displayUserList(users);
    }

    public void addUser() {
        checkPermission(UserRole.ADMIN);
        String username = view.getInput("Enter new username: ");
        if (users.containsKey(username)) {
            view.displayMessage("Username already exists!");
            return;
        }
        String password = view.getInput("Enter password: ");
        String roleInput = view.getInput("Enter role (ADMIN/TEACHER/STUDENT): ");
        try {
            UserRole role = UserRole.valueOf(roleInput.toUpperCase());
            users.put(username, new User(username, password, role));
            view.displayMessage("User added successfully!");
        } catch (IllegalArgumentException e) {
            view.displayMessage("Invalid role! Use ADMIN, TEACHER, or STUDENT.");
        }
    }

    public void addSubject() {
        checkPermission(UserRole.ADMIN);
        String subjectId = view.getInput("Enter subject ID: ");
        if (subjects.containsKey(subjectId)) {
            view.displayMessage("Subject ID already exists!");
            return;
        }
        String subjectName = view.getInput("Enter subject name: ");
        int credits = Integer.parseInt(view.getInput("Enter credits: "));
        subjects.put(subjectId, new Subject(subjectId, subjectName, credits));
        view.displayMessage("Subject added successfully!");
    }

    public void addStudent() {
        checkPermission(UserRole.ADMIN);
        String id = view.getInput("Enter student ID: ");
        if (students.containsKey(id)) {
            view.displayMessage("Student ID already exists!");
            return;
        }
        String name = view.getInput("Enter student name: ");
        LocalDate birthDate = LocalDate.parse(view.getInput("Enter birth date (YYYY-MM-DD): "));
        String classId = view.getInput("Enter class ID: ");
        if (!classes.containsKey(classId)) {
            view.displayMessage("Class not found!");
            return;
        }
        String bankAccount = view.getInput("Enter bank account number: ");
        students.put(id, new Student(id, name, birthDate, classId, bankAccount));
        users.put(id, new User(id, "student123", UserRole.STUDENT));
        view.displayMessage("Student added successfully!");
    }

    public void addClass() {
        checkPermission(UserRole.ADMIN);
        String classId = view.getInput("Enter class ID: ");
        if (classes.containsKey(classId)) {
            view.displayMessage("Class ID already exists!");
            return;
        }
        String teacher = view.getInput("Enter teacher username: ");
        if (!users.containsKey(teacher) || users.get(teacher).getRole() != UserRole.TEACHER) {
            view.displayMessage("Invalid teacher username!");
            return;
        }
        classes.put(classId, teacher);
        view.displayMessage("Class added successfully!");
    }

    public void addGrade() {
        checkPermission(UserRole.TEACHER);
        String studentId = view.getInput("Enter student ID: ");
        if (!students.containsKey(studentId)) {
            view.displayMessage("Student not found!");
            return;
        }
        String subjectId = view.getInput("Enter subject ID: ");
        if (!subjects.containsKey(subjectId)) {
            view.displayMessage("Subject not found!");
            return;
        }
        double score = Double.parseDouble(view.getInput("Enter score (0-100): "));
        grades.add(new Grade(studentId, subjectId, score, LocalDate.now()));
        users.get(studentId).addNotification("New grade added for " + subjectId + ": " + score);
        view.displayMessage("Grade added successfully!");
    }

    public void addSchedule() {
        checkPermission(UserRole.ADMIN);
        String classId = view.getInput("Enter class ID: ");
        if (!classes.containsKey(classId)) {
            view.displayMessage("Class not found!");
            return;
        }
        String subjectId = view.getInput("Enter subject ID: ");
        if (!subjects.containsKey(subjectId)) {
            view.displayMessage("Subject not found!");
            return;
        }
        LocalDateTime startTime = LocalDateTime.parse(view.getInput("Enter start time (YYYY-MM-DD HH:MM): "),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        int duration = Integer.parseInt(view.getInput("Enter duration (minutes): "));

        LocalDateTime endTime = startTime.plusMinutes(duration);
        for (Schedule s : schedules) {
            if (s.toString().contains(classId) &&
                    !(endTime.isBefore(s.getStartTime()) || startTime.isAfter(s.getEndTime()))) {
                view.displayMessage("Schedule conflict detected with " + s);
                return;
            }
        }
        schedules.add(new Schedule(classId, subjectId, startTime, duration));
        students.values().stream()
                .filter(s -> s.getClassId().equals(classId))
                .forEach(s -> users.get(s.getId()).addNotification("New schedule: " + subjectId + " at " + startTime));
        view.displayMessage("Schedule added successfully!");
    }

    public void manageTuition() {
        checkPermission(UserRole.ADMIN);
        String studentId = view.getInput("Enter student ID: ");
        if (!students.containsKey(studentId)) {
            view.displayMessage("Student not found!");
            return;
        }
        double amount = Double.parseDouble(view.getInput("Enter amount: "));
        LocalDate dueDate = LocalDate.parse(view.getInput("Enter due date (YYYY-MM-DD): "));
        tuitions.add(new Tuition(studentId, amount, dueDate, students.get(studentId).getBankAccount()));
        users.get(studentId).addNotification("New tuition fee: $" + amount + ", due " + dueDate);
        view.displayMessage("Tuition added successfully!");
    }

    public void viewTuition() {
        String studentId = currentUser.getRole() == UserRole.STUDENT ? currentUser.getUsername() : null;
        if (studentId == null) {
            checkPermission(UserRole.ADMIN);
            studentId = view.getInput("Enter student ID: ");
        }
        view.displayTuition(tuitions, studentId, students.get(studentId).getName());
    }

    public void payTuition() {
        checkPermission(UserRole.STUDENT);
        String studentId = currentUser.getUsername();
        List<Tuition> unpaid = tuitions.stream()
                .filter(t -> t.getStudentId().equals(studentId) && !t.isPaid())
                .collect(Collectors.toList());
        if (unpaid.isEmpty()) {
            view.displayMessage("No unpaid tuition!");
            return;
        }
        view.displayUnpaidTuition(unpaid);
        int index = Integer.parseInt(view.getInput("Select tuition to pay (number): ")) - 1;
        unpaid.get(index).setPaid(true);
        users.get(studentId).addNotification("Tuition paid: $" + unpaid.get(index).getAmount());
        view.displayMessage("Tuition paid successfully!");
    }

    public void recordAttendance() {
        checkPermission(UserRole.TEACHER);
        int scheduleIndex = Integer.parseInt(view.getInput("Enter schedule index (from view schedule): "));
        if (scheduleIndex < 0 || scheduleIndex >= schedules.size()) {
            view.displayMessage("Invalid schedule index!");
            return;
        }
        Schedule schedule = schedules.get(scheduleIndex);
        String classId = schedule.toString().split("\\|")[1].trim();
        view.displayMessage("Recording attendance for " + schedule);
        students.values().stream()
                .filter(s -> s.getClassId().equals(classId))
                .forEach(s -> {
                    boolean present = view.getInput("Is " + s.getId() + " present? (y/n): ").equalsIgnoreCase("y");
                    attendances.add(new Attendance(s.getId(), String.valueOf(scheduleIndex),
                            LocalDateTime.now(), present));
                    if (!present) {
                        users.get(s.getId()).addNotification("Missed class: " + schedule);
                    }
                });
        view.displayMessage("Attendance recorded successfully!");
    }

    public void viewAttendance() {
        String studentId = currentUser.getRole() == UserRole.STUDENT ? currentUser.getUsername() : null;
        if (studentId == null) {
            studentId = view.getInput("Enter student ID: ");
        }
        view.displayAttendance(attendances, studentId, students.get(studentId).getName());
    }

    public void predictPerformance() {
        checkPermission(UserRole.TEACHER);
        String studentId = view.getInput("Enter student ID: ");
        if (!students.containsKey(studentId)) {
            view.displayMessage("Student not found!");
            return;
        }
        List<Grade> studentGrades = grades.stream()
                .filter(g -> g.getStudentId().equals(studentId))
                .collect(Collectors.toList());
        if (studentGrades.isEmpty()) {
            view.displayMessage("No grades available for prediction!");
            return;
        }
        double avgScore = studentGrades.stream().mapToDouble(Grade::getScore).average().orElse(0.0);
        long attendanceRate = attendances.stream()
                .filter(a -> a.getStudentId().equals(studentId) && a.isPresent())
                .count() * 100 / Math.max(1, attendances.stream()
                .filter(a -> a.getStudentId().equals(studentId)).count());
        double predictedGPA = (avgScore / 25) * 0.7 + (attendanceRate / 100.0) * 1.3;
        view.displayMessage(String.format("Predicted GPA for %s: %.2f (Current Avg: %.2f, Attendance: %d%%)",
                students.get(studentId).getName(), predictedGPA, avgScore, attendanceRate));
    }

    public void viewTranscript() {
        String studentId = currentUser.getRole() == UserRole.STUDENT ? currentUser.getUsername() : null;
        if (studentId == null) {
            studentId = view.getInput("Enter student ID: ");
        }
        if (!students.containsKey(studentId)) {
            view.displayMessage("Student not found!");
            return;
        }
        view.displayTranscript(grades, subjects, studentId, students.get(studentId).getName());
    }

    public void viewSchedule() {
        String classId = currentUser.getRole() == UserRole.STUDENT ?
                students.get(currentUser.getUsername()).getClassId() : null;
        if (classId == null) {
            classId = view.getInput("Enter class ID: ");
        }
        if (!classes.containsKey(classId)) {
            view.displayMessage("Class not found!");
            return;
        }
        view.displaySchedule(schedules, classId);
    }

    public void generateReport() {
        checkPermission(UserRole.ADMIN);
        view.generateReport(classes, grades, tuitions, attendances, students);
    }

    private void checkPermission(UserRole requiredRole) {
        if (currentUser.getRole() != requiredRole) {
            view.displayMessage("Permission denied! Required role: " + requiredRole);
            throw new SecurityException("Unauthorized access");
        }
    }

    public User getCurrentUser() { return currentUser; }
}