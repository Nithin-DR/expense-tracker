package com.nkoder.expense_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRequestDTO {

    @NotBlank(message = "Title can not be blank")
    private String title;

    @Positive(message = "Amount must be positive")
    private double amount;

    @NotNull(message = "Date is mandatory")
    private LocalDate expenseDate;
}
