package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.dto.ExpenseRequestDTO;
import com.nkoder.expense_tracker.dto.ExpenseResponseDTO;
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

    // CREATE
    @Override
    public ExpenseResponseDTO saveExpense(ExpenseRequestDTO request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setUser(user);

        return toResponseDTO(expenseRepo.save(expense));
    }

    // GET ALL
    @Override
    public List<ExpenseResponseDTO> getExpensesForUser(String username) {
        return expenseRepo.findByUserUsername(username)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // GET BY ID
    @Override
    public Optional<ExpenseResponseDTO> getExpenseById(Long id, String username) {
        return expenseRepo.findByIdAndUserUsername(id, username)
                .map(this::toResponseDTO);
    }

    // UPDATE
    @Override
    public Optional<ExpenseResponseDTO> updateExpense(
            Long id, ExpenseRequestDTO request, String username) {

        return expenseRepo.findByIdAndUserUsername(id, username)
                .map(existing -> {
                    existing.setTitle(request.getTitle());
                    existing.setAmount(request.getAmount());
                    existing.setExpenseDate(request.getExpenseDate());
                    return toResponseDTO(expenseRepo.save(existing));
                });
    }

    // DELETE
    @Override
    public void deleteExpense(Long id, String username) {
        Expense expense = expenseRepo.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        expenseRepo.delete(expense);
    }

    // ENTITY → RESPONSE DTO
    private ExpenseResponseDTO toResponseDTO(Expense expense) {
        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getExpenseDate()
        );
    }
}
