package com.nkoder.expense_tracker.model;

import com.nkoder.expense_tracker.model.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // email for OAuth users

    @Column(nullable = true)
    private String password; // NULL for Google users

    private String role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider; // LOCAL, GOOGLE

    private String providerId; // Google "sub"

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Expense> expenses;
}


