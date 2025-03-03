package com.tien.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private LocalDate birthDate;
    private String classId;
    private String bankAccount;

    public Student(String id, String name, LocalDate birthDate, String classId, String bankAccount) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.classId = classId;
        this.bankAccount = bankAccount;
    }

    public String getId() { return id; }
    public String getClassId() { return classId; }
    public String getBankAccount() { return bankAccount; }
    public String getName() { return name; }
    @Override
    public String toString() {
        return String.format("| %-8s | %-20s | %-10s | %-6s | %-15s |",
                id, name, birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE), classId, bankAccount);
    }
}
