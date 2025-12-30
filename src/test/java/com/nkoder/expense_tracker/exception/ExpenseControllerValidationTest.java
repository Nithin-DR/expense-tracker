package com.nkoder.expense_tracker.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nkoder.expense_tracker.controller.ExpenseController;
import com.nkoder.expense_tracker.dto.ExpenseRequestDTO;
import com.nkoder.expense_tracker.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ExpenseController.class)
@Import(GlobalExceptionHandler.class)
class ExpenseControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExpenseService expenseService;

    // ---------- VALIDATION TEST ----------

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {

        ExpenseRequestDTO invalidRequest = new ExpenseRequestDTO();
        invalidRequest.setTitle("");              //  @NotBlank
        invalidRequest.setAmount(-100);            //  @Positive
        invalidRequest.setExpenseDate(null);       //  @NotNull

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title")
                        .value("Title can not be blank"))
                .andExpect(jsonPath("$.amount")
                        .value("Amount must be positive"))
                .andExpect(jsonPath("$.expenseDate")
                        .value("Date is mandatory"));
    }
}
