package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.model.Expense;
import com.nkoder.expense_tracker.repo.ExpenseRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  //-- Integrates Mockito with JUnit 5x
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepo expenseRepo;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    // ---------- saveExpense ----------

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        Expense expense = new Expense();
        expense.setTitle("Food");
        expense.setAmount(-250.0);
        expense.setExpenseDate(LocalDate.now());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> expenseService.saveExpense(expense)
        );

        assertEquals("Amount must be greater than zero", exception.getMessage());

        verify(expenseRepo, never()).save(any());
    }



    @Test
    void shouldReturnAllExpenses() {
        Expense e1 = new Expense();
        Expense e2 = new Expense();

        when(expenseRepo.findAll()).thenReturn(List.of(e1, e2));

        List<Expense> expenses = expenseService.getAllExpenses();

        assertEquals(2, expenses.size());
        verify(expenseRepo, times(1)).findAll();
    }

    // ---------- getExpenseById ----------

    @Test
    void shouldReturnExpenseWhenIdExists() {
        Expense expense = new Expense();
        expense.setId(1L);

        when(expenseRepo.findById(1L)).thenReturn(Optional.of(expense));

        Optional<Expense> result = expenseService.getExpenseById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenExpenseNotFound() {
        when(expenseRepo.findById(99L)).thenReturn(Optional.empty());

        Optional<Expense> result = expenseService.getExpenseById(99L);

        assertTrue(result.isEmpty());
    }

    // ---------- deleteExpense ----------

    @Test
    void shouldDeleteExpenseById() {
        doNothing().when(expenseRepo).deleteById(1L);

        expenseService.deleteExpense(1L);

        verify(expenseRepo, times(1)).deleteById(1L);
    }

    // ---------- updateExpense ----------

    @Test
    void shouldUpdateExpenseWhenExists() {
        Expense existing = new Expense();
        existing.setId(1L);
        existing.setTitle("Old");
        existing.setAmount(100.0);

        Expense updated = new Expense();
        updated.setTitle("New");
        updated.setAmount(200.0);
        updated.setExpenseDate(LocalDate.now());

        when(expenseRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(expenseRepo.save(existing)).thenReturn(existing);

        Optional<Expense> result = expenseService.updateExpense(1L, updated);

        assertTrue(result.isPresent());
        assertEquals("New", result.get().getTitle());
        assertEquals(200.0, result.get().getAmount());
        verify(expenseRepo, times(1)).save(existing);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExistingExpense() {
        Expense updated = new Expense();

        when(expenseRepo.findById(99L)).thenReturn(Optional.empty());

        Optional<Expense> result = expenseService.updateExpense(99L, updated);

        assertTrue(result.isEmpty());
        verify(expenseRepo, never()).save(any());
    }
}
