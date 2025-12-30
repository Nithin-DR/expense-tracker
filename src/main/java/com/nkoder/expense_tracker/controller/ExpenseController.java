package com.nkoder.expense_tracker.controller;

import com.nkoder.expense_tracker.dto.ExpenseRequestDTO;
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
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> addExpense(
            @Valid @RequestBody ExpenseRequestDTO request) {

        return ResponseEntity.ok(
                expenseService.saveExpense(request, currentUsername())
        );
    }

    // GET ALL
    @GetMapping
    public List<ExpenseResponseDTO> getAllExpenses() {
        return expenseService.getExpensesForUser(currentUsername());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable Long id) {

        return expenseService.getExpenseById(id, currentUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequestDTO request) {

        return expenseService.updateExpense(id, request, currentUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id, currentUsername());
        return ResponseEntity.noContent().build();
    }
}



    //ResponseEntity = data + status, along with data it will return that status of http
    //ResponseEntity is used to send proper HTTP status codes along with response data.




