package com.tien;

import com.tien.controller.SchoolController;
import com.tien.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        SchoolController controller = new SchoolController(view);

        while (!controller.login(view.getInput("Enter username: "), view.getInput("Enter password: "))) {
            view.displayMessage("Please try again.");
        }

        while (true) {
            view.displayMenu(controller.getCurrentUser());
            try {
                int choice = Integer.parseInt(view.getInput(""));
                switch (choice) {
                    case 1: controller.addUser(); break;
                    case 2: controller.addSubject(); break;
                    case 3: controller.addStudent(); break;
                    case 4: controller.addClass(); break;
                    case 5: controller.addGrade(); break;
                    case 6: controller.addSchedule(); break;
                    case 7: controller.manageTuition(); break;
                    case 8: controller.viewTuition(); break;
                    case 9: controller.payTuition(); break;
                    case 10: controller.recordAttendance(); break;
                    case 11: controller.viewAttendance(); break;
                    case 12: controller.predictPerformance(); break;
                    case 13: controller.viewTranscript(); break;
                    case 14: controller.viewSchedule(); break;
                    case 15: controller.generateReport(); break;
                    case 16: controller.listUsers(); break;
                    case 17:
                        controller.saveData();
                        view.displayMessage("Logged out!");
                        return;
                }
            } catch (Exception e) {
                view.displayMessage("Error: " + e.getMessage());
            }
        }
    }
}