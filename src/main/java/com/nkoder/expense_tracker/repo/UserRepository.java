package com.nkoder.expense_tracker.repo;

import com.nkoder.expense_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nkoder.expense_tracker.model.AuthProvider;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndProvider(String username, AuthProvider provider);


}