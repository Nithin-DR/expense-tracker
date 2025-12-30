package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.dto.ExpenseRequestDTO;
import com.nkoder.expense_tracker.dto.ExpenseResponseDTO;
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

    private static final String USERNAME = "nithin";

    // ---------- saveExpense ----------

    @Test
    void shouldSaveExpenseSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);

        ExpenseRequestDTO request = new ExpenseRequestDTO();
        request.setTitle("Food");
        request.setAmount(250.0);
        request.setExpenseDate(LocalDate.now());

        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(user));

        when(expenseRepo.save(any(Expense.class)))
                .thenAnswer(invocation -> {
                    Expense e = invocation.getArgument(0);
                    e.setId(1L);
                    return e;
                });

        ExpenseResponseDTO response =
                expenseService.saveExpense(request, USERNAME);

        assertNotNull(response);
        assertEquals("Food", response.getTitle());
        assertEquals(250.0, response.getAmount());

        verify(userRepository, times(1)).findByUsername(USERNAME);
        verify(expenseRepo, times(1)).save(any(Expense.class));
    }

    // ---------- getExpensesForUser ----------

    @Test
    void shouldReturnExpensesForUser() {

        Expense e1 = new Expense();
        e1.setId(1L);
        Expense e2 = new Expense();
        e2.setId(2L);

        when(expenseRepo.findByUserUsername(USERNAME))
                .thenReturn(List.of(e1, e2));

        List<ExpenseResponseDTO> expenses =
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
        expense.setTitle("Food");

        when(expenseRepo.findByIdAndUserUsername(1L, USERNAME))
                .thenReturn(Optional.of(expense));

        Optional<ExpenseResponseDTO> result =
                expenseService.getExpenseById(1L, USERNAME);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Food", result.get().getTitle());
    }

    @Test
    void shouldReturnEmptyWhenExpenseNotFoundForUser() {

        when(expenseRepo.findByIdAndUserUsername(99L, USERNAME))
                .thenReturn(Optional.empty());

        Optional<ExpenseResponseDTO> result =
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

        ExpenseRequestDTO request = new ExpenseRequestDTO();
        request.setTitle("New");
        request.setAmount(200.0);
        request.setExpenseDate(LocalDate.now());

        when(expenseRepo.findByIdAndUserUsername(1L, USERNAME))
                .thenReturn(Optional.of(existing));

        when(expenseRepo.save(existing))
                .thenReturn(existing);

        Optional<ExpenseResponseDTO> result =
                expenseService.updateExpense(1L, request, USERNAME);

        assertTrue(result.isPresent());
        assertEquals("New", result.get().getTitle());
        assertEquals(200.0, result.get().getAmount());

        verify(expenseRepo, times(1)).save(existing);
    }

    @Test
    void shouldReturnEmptyWhenUpdatingNonExistingExpense() {

        ExpenseRequestDTO request = new ExpenseRequestDTO();

        when(expenseRepo.findByIdAndUserUsername(99L, USERNAME))
                .thenReturn(Optional.empty());

        Optional<ExpenseResponseDTO> result =
                expenseService.updateExpense(99L, request, USERNAME);

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
