package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.dto.ExpenseRequestDTO;
import com.nkoder.expense_tracker.dto.ExpenseResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    ExpenseResponseDTO saveExpense(ExpenseRequestDTO request, String username);

    List<ExpenseResponseDTO> getExpensesForUser(String username);

    Optional<ExpenseResponseDTO> getExpenseById(Long id, String username);

    Optional<ExpenseResponseDTO> updateExpense(
            Long id, ExpenseRequestDTO request, String username);

    void deleteExpense(Long id, String username);
}
