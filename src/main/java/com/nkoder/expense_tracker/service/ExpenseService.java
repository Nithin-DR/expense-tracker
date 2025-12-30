package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.model.Expense;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface ExpenseService {

    Expense saveExpense(Expense expense, String username);

    List<Expense> getExpensesForUser(String username);

    Optional<Expense> getExpenseById(Long id, String username);

    Optional<Expense> updateExpense(Long id, Expense expense, String username);

    void deleteExpense(Long id, String username);
}

