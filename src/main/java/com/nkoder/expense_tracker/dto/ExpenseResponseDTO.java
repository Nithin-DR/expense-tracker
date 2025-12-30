package com.nkoder.expense_tracker.dto;

import java.time.LocalDate;

public class ExpenseResponseDTO {

    private Long id;
    private String title;
    private double amount;
    private LocalDate expenseDate;

    public ExpenseResponseDTO(Long id, String title, double amount, LocalDate expenseDate) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.expenseDate = expenseDate;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public double getAmount() { return amount; }
    public LocalDate getExpenseDate() { return expenseDate; }
}