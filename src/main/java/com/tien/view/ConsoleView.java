package com.tien.view;

import com.tien.model.*;

import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class ConsoleView {
    private Scanner scanner;

    public ConsoleView() {
        scanner = new Scanner(System.in);
    }

    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayNotifications(List<String> notifications) {
        if (!notifications.isEmpty()) {
            System.out.println("\n=== NOTIFICATIONS ===");
            notifications.forEach(System.out::println);
        }
    }

    public void displayTuition(List<Tuition> tuitions, String studentId, String studentName) {
        System.out.println("\n=== TUITION FOR " + studentName + " ===");
        System.out.println("| Amount    | Due Date   | Status  | Bank Account     |");
        System.out.println("|-----------|------------|---------|------------------|");
        tuitions.stream()
                .filter(t -> t.getStudentId().equals(studentId))
                .forEach(t -> System.out.printf("| $%8.2f | %-10s | %-7s | %-15s |\n",
                        t.getAmount(), t.getDueDate(), t.isPaid() ? "Paid" : "Unpaid", t.getBankAccount()));
    }

    public void displayUnpaidTuition(List<Tuition> unpaid) {
        unpaid.forEach(t -> System.out.printf("%d. $%.2f due %s (Bank: %s)\n",
                unpaid.indexOf(t) + 1, t.getAmount(), t.getDueDate(), t.getBankAccount()));
    }

    public void displayAttendance(List<Attendance> attendances, String studentId, String studentName) {
        System.out.println("\n=== ATTENDANCE FOR " + studentName + " ===");
        System.out.println("| Student ID | Check-in Time      | Present |");
        System.out.println("|------------|--------------------|---------|");
        attendances.stream()
                .filter(a -> a.getStudentId().equals(studentId))
                .forEach(System.out::println);
        long presentCount = attendances.stream()
                .filter(a -> a.getStudentId().equals(studentId) && a.isPresent())
                .count();
        System.out.println("Attendance Rate: " + (presentCount * 100 / Math.max(1, attendances.size())) + "%");
    }

    public void displayTranscript(List<Grade> grades, HashMap<String, Subject> subjects, String studentId, String studentName) {
        System.out.println("\n=== TRANSCRIPT FOR " + studentName + " ===");
        System.out.println("| Subject ID | Subject Name         | Credits | Score | Date       |");
        System.out.println("|------------|----------------------|---------|-------|------------|");
        double totalScore = 0;
        int totalCredits = 0;
        for (Grade grade : grades) {
            if (grade.getStudentId().equals(studentId)) {
                Subject subject = subjects.get(grade.getSubjectId());
                System.out.printf("| %-10s | %-20s | %7d | %5.2f | %-10s |\n",
                        subject.getSubjectId(), subject.getSubjectName(),
                        subject.getCredits(), grade.getScore(), grade.getDate());
                totalScore += grade.getScore() * subject.getCredits();
                totalCredits += subject.getCredits();
            }
        }
        if (totalCredits > 0) {
            System.out.printf("\nGPA: %.2f (on 4.0 scale)\n", totalScore / totalCredits / 25);
        }
    }

    public void displaySchedule(List<Schedule> schedules, String classId) {
        System.out.println("\n=== SCHEDULE FOR CLASS " + classId + " ===");
        System.out.println("| Index | Class  | Subject | Start Time         | Duration |");
        System.out.println("|-------|--------|---------|--------------------|----------|");
        schedules.stream()
                .filter(s -> s.toString().contains(classId))
                .sorted(Comparator.comparing(Schedule::getStartTime))
                .forEach(s -> System.out.printf("| %5d %s\n", schedules.indexOf(s), s));
    }

    public void generateReport(HashMap<String, String> classes, List<Grade> grades, List<Tuition> tuitions,
                               List<Attendance> attendances, HashMap<String, Student> students) {
        try (PrintWriter writer = new PrintWriter("school_report.txt")) {
            writer.println("=== SCHOOL PERFORMANCE REPORT ===");
            writer.println("Generated on: " + LocalDateTime.now());
            classes.forEach((classId, teacher) -> {
                writer.println("\nClass: " + classId + " | Teacher: " + teacher);
                double classAvg = grades.stream()
                        .filter(g -> students.get(g.getStudentId()).getClassId().equals(classId))
                        .mapToDouble(Grade::getScore)
                        .average()
                        .orElse(0.0);
                writer.printf("Class Average Score: %.2f%n", classAvg);
                double tuitionCollected = tuitions.stream()
                        .filter(t -> students.get(t.getStudentId()).getClassId().equals(classId) && t.isPaid())
                        .mapToDouble(Tuition::getAmount)
                        .sum();
                writer.printf("Tuition Collected: $%.2f%n", tuitionCollected);
                long attendanceRate = attendances.stream()
                        .filter(a -> students.get(a.getStudentId()).getClassId().equals(classId) && a.isPresent())
                        .count() * 100 / Math.max(1, attendances.stream()
                        .filter(a -> students.get(a.getStudentId()).getClassId().equals(classId)).count());
                writer.printf("Class Attendance Rate: %d%%\n", attendanceRate);
            });
            System.out.println("Report generated at school_report.txt");
        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    public void displayEvents(List<Event> events) {
        System.out.println("\n=== SCHOOL EVENTS ===");
        System.out.println("| Event ID  | Name                 | Start Time         | Description                    |");
        System.out.println("|-----------|----------------------|--------------------|--------------------------------|");
        events.stream()
                .sorted(Comparator.comparing(Event::getStartTime))
                .forEach(System.out::println);
    }

    public void displayDocuments(List<Document> documents, String userId, boolean canUpload) {
        System.out.println("\n=== DOCUMENTS ===");
        System.out.println("| Doc ID    | Title                | Uploader   | File Name            |");
        System.out.println("|-----------|----------------------|------------|----------------------|");
        documents.forEach(d -> {
            if (canUpload || d.getUploaderId().equals(userId)) {
                System.out.println(d);
            }
        });
    }

    public void displayUserList(TreeMap<String, User> users) {
        if (users.isEmpty()) {
            System.out.println("No users in the system!");
            return;
        }
        System.out.println("\n=== USER LIST ===");
        System.out.println("| Username         | Role     | Password     |");
        System.out.println("|------------------|----------|--------------|");
        users.forEach((username, user) ->
                System.out.printf("| %-16s | %-8s | %-16s| \n", username, user.getRole(), user.getPassword()));
    }

    public void displayMenu(User currentUser) {
        System.out.println("\n=== SCHOOL MANAGEMENT SYSTEM ===");
        System.out.println("User: " + currentUser.getUsername() + " | Role: " + currentUser.getRole());
        System.out.println("1. Add user (Admin)");
        System.out.println("2. Add subject (Admin)");
        System.out.println("3. Add student (Admin)");
        System.out.println("4. Add class (Admin)");
        System.out.println("5. Add grade (Teacher)");
        System.out.println("6. Add schedule (Admin)");
        System.out.println("7. Manage tuition (Admin)");
        System.out.println("8. View tuition");
        System.out.println("9. Pay tuition (Student)");
        System.out.println("10. Record attendance (Teacher)");
        System.out.println("11. View attendance");
        System.out.println("12. Predict performance (Teacher)");
        System.out.println("13. View transcript");
        System.out.println("14. View schedule");
        System.out.println("15. Generate report (Admin)");
        System.out.println("16. List users (Admin)");
        System.out.println("17. Add event (Admin)");
        System.out.println("18. View events");
        System.out.println("19. Upload document");
        System.out.println("20. View documents");
        System.out.println("21. Logout");
        System.out.print("Enter your choice: ");
    }
}