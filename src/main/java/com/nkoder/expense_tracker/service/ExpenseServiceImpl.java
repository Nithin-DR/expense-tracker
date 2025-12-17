package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.model.Expense;
import com.nkoder.expense_tracker.repo.ExpenseRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepo expenseRepo;

    public ExpenseServiceImpl(ExpenseRepo expenseRepository) {
        this.expenseRepo = expenseRepository;
    }

    @Override
    public Expense saveExpense(Expense expense) {
        return expenseRepo.save(expense);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepo.findAll();
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepo.deleteById(id);
    }



    @Override
    public Optional<Expense> updateExpense(Long id, Expense expense) {
        return expenseRepo.findById(id).map(existing -> {
            existing.setTitle(expense.getTitle());
            existing.setAmount(expense.getAmount());
            existing.setExpenseDate(expense.getExpenseDate());
            return expenseRepo.save(existing);
        });
    }
    //Find by ID
    //If exists → update → save
    //If not → return 404
    //Optional avoids null
    //ResponseEntity controls status


}
