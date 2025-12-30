package com.nkoder.expense_tracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExpenseResponseDTO {

    private Long id;
    private String title;
    private double amount;
    private LocalDate expenseDate;
}
