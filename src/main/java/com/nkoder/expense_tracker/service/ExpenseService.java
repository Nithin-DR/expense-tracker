package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.model.Expense;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface ExpenseService {

    Expense saveExpense(Expense expense);
    List<Expense> getAllExpenses();
    void deleteExpense(Long  exId);

    Optional<Expense> updateExpense(Long id, Expense expense);

    Optional<Expense> getExpenseById(Long id);

}
