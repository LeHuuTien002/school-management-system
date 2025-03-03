package com.tien.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Tuition implements Serializable {
    private static final long serialVersionUID = 1L;
    private String studentId;
    private double amount;
    private LocalDate dueDate;
    private boolean paid;
    private String bankAccount;

    public Tuition(String studentId, double amount, LocalDate dueDate, String bankAccount) {
        this.studentId = studentId;
        this.amount = amount;
        this.dueDate = dueDate;
        this.paid = false;
        this.bankAccount = bankAccount;
    }

    public String getStudentId() { return studentId; }
    public double getAmount() { return amount; }
    public LocalDate getDueDate() { return dueDate; }
    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }
    public String getBankAccount() { return bankAccount; }
}
