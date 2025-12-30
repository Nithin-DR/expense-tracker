package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.model.Expense;
import com.nkoder.expense_tracker.model.User;
import com.nkoder.expense_tracker.repo.ExpenseRepo;
import com.nkoder.expense_tracker.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepo expenseRepo;
    private final UserRepository userRepository;

    public ExpenseServiceImpl(ExpenseRepo expenseRepo,
                              UserRepository userRepository) {
        this.expenseRepo = expenseRepo;
        this.userRepository = userRepository;
    }

    @Override
    public Expense saveExpense(Expense expense, String username) {

        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        expense.setUser(user);

        return expenseRepo.save(expense);
    }

    @Override
    public List<Expense> getExpensesForUser(String username) {
        return expenseRepo.findByUserUsername(username);
    }

    @Override
    public Optional<Expense> getExpenseById(Long id, String username) {
        return expenseRepo.findByIdAndUserUsername(id, username);
    }

    @Override
    public Optional<Expense> updateExpense(Long id, Expense expense, String username) {
        return expenseRepo.findByIdAndUserUsername(id, username)
                .map(existing -> {
                    existing.setTitle(expense.getTitle());
                    existing.setAmount(expense.getAmount());
                    existing.setExpenseDate(expense.getExpenseDate());
                    return expenseRepo.save(existing);
                });
    }

    @Override
    public void deleteExpense(Long id, String username) {
        Expense expense = expenseRepo.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        expenseRepo.delete(expense);
    }
}


