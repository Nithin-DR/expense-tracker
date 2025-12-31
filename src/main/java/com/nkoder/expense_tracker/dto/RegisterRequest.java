package com.nkoder.expense_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message ="username can not be blank")
    private String username;
    @NotBlank(message = "Password can not be blank")
    private String password;
    @NotBlank(message = "role can not be blank")
    private String role;
}
