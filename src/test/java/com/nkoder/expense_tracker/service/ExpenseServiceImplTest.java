package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.model.Expense;
import com.nkoder.expense_tracker.model.User;
import com.nkoder.expense_tracker.repo.ExpenseRepo;
import com.nkoder.expense_tracker.repo.UserRepository;
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
@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepo expenseRepo;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private final String USERNAME = "nithin";

    // ---------- saveExpense ----------

    @Test
    void shouldSaveExpenseSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);

        Expense expense = new Expense();
        expense.setTitle("Food");
        expense.setAmount(250.0);
        expense.setExpenseDate(LocalDate.now());

        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(user));

        when(expenseRepo.save(any(Expense.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Expense saved = expenseService.saveExpense(expense, USERNAME);

        assertNotNull(saved);
        assertEquals("Food", saved.getTitle());
        assertEquals(user, saved.getUser());

        verify(userRepository, times(1)).findByUsername(USERNAME);
        verify(expenseRepo, times(1)).save(expense);
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {

        Expense expense = new Expense();
        expense.setTitle("Food");
        expense.setAmount(-250.0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> expenseService.saveExpense(expense, USERNAME)
        );

        assertEquals("Amount must be greater than zero", exception.getMessage());

        verify(expenseRepo, never()).save(any());
        verify(userRepository, never()).findByUsername(any());
    }

    // ---------- getExpensesForUser ----------

    @Test
    void shouldReturnExpensesForUser() {

        Expense e1 = new Expense();
        Expense e2 = new Expense();

        when(expenseRepo.findByUserUsername(USERNAME))
                .thenReturn(List.of(e1, e2));

        List<Expense> expenses =
                expenseService.getExpensesForUser(USERNAME);

        assertEquals(2, expenses.size());
        verify(expenseRepo, times(1))
                .findByUserUsername(USERNAME);
    }

    // ---------- getExpenseById ----------

    @Test
    void shouldReturnExpenseWhenIdExistsForUser() {

        Expense expense = new Expense();
        expense.setId(1L);

        when(expenseRepo.findByIdAndUserUsername(1L, USERNAME))
                .thenReturn(Optional.of(expense));

        Optional<Expense> result =
                expenseService.getExpenseById(1L, USERNAME);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenExpenseNotFoundForUser() {

        when(expenseRepo.findByIdAndUserUsername(99L, USERNAME))
                .thenReturn(Optional.empty());

        Optional<Expense> result =
                expenseService.getExpenseById(99L, USERNAME);

        assertTrue(result.isEmpty());
    }

    // ---------- updateExpense ----------

    @Test
    void shouldUpdateExpenseWhenExistsForUser() {

        Expense existing = new Expense();
        existing.setId(1L);
        existing.setTitle("Old");
        existing.setAmount(100.0);

        Expense updated = new Expense();
        updated.setTitle("New");
        updated.setAmount(200.0);
        updated.setExpenseDate(LocalDate.now());

        when(expenseRepo.findByIdAndUserUsername(1L, USERNAME))
                .thenReturn(Optional.of(existing));

        when(expenseRepo.save(existing))
                .thenReturn(existing);

        Optional<Expense> result =
                expenseService.updateExpense(1L, updated, USERNAME);

        assertTrue(result.isPresent());
        assertEquals("New", result.get().getTitle());
        assertEquals(200.0, result.get().getAmount());

        verify(expenseRepo, times(1)).save(existing);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExistingExpense() {

        Expense updated = new Expense();

        when(expenseRepo.findByIdAndUserUsername(99L, USERNAME))
                .thenReturn(Optional.empty());

        Optional<Expense> result =
                expenseService.updateExpense(99L, updated, USERNAME);

        assertTrue(result.isEmpty());
        verify(expenseRepo, never()).save(any());
    }

    // ---------- deleteExpense ----------

    @Test
    void shouldDeleteExpenseForUser() {

        Expense expense = new Expense();
        expense.setId(1L);

        when(expenseRepo.findByIdAndUserUsername(1L, USERNAME))
                .thenReturn(Optional.of(expense));

        expenseService.deleteExpense(1L, USERNAME);

        verify(expenseRepo, times(1)).delete(expense);
    }
}

