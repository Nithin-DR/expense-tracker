package com.nkoder.expense_tracker.controller;

import com.nkoder.expense_tracker.dto.ExpenseResponseDTO;
import com.nkoder.expense_tracker.model.Expense;
import com.nkoder.expense_tracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    private String currentUsername() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    // 🔐 ADD EXPENSE
    @PostMapping
    public ExpenseResponseDTO addExpense(@Valid @RequestBody Expense expense) {

        Expense saved =
                expenseService.saveExpense(expense, currentUsername());

        return toDto(saved);
    }

    // 🔐 GET ALL USER EXPENSES
    @GetMapping
    public List<ExpenseResponseDTO> getAllExpenses() {

        return expenseService
                .getExpensesForUser(currentUsername())
                .stream()
                .map(this::toDto)
                .toList();
    }

    // 🔐 GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable Long id) {

        return expenseService
                .getExpenseById(id, currentUsername())
                .map(e -> ResponseEntity.ok(toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔐 UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody Expense expense) {

        return expenseService
                .updateExpense(id, expense, currentUsername())
                .map(e -> ResponseEntity.ok(toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔐 DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id, currentUsername());
        return ResponseEntity.noContent().build();
    }

    // 🔁 ENTITY → DTO
    private ExpenseResponseDTO toDto(Expense e) {
        return new ExpenseResponseDTO(
                e.getId(),
                e.getTitle(),
                e.getAmount(),
                e.getExpenseDate()
        );
    }
}


    //ResponseEntity = data + status, along with data it will return that status of http
    //ResponseEntity is used to send proper HTTP status codes along with response data.




