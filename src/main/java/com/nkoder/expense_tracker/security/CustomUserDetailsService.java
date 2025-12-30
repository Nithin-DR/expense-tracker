package com.nkoder.expense_tracker.security;

import com.nkoder.expense_tracker.model.User;
import com.nkoder.expense_tracker.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
//It retrieves user information from the database and adapts it into Spring Security’s UserDetails model for authentication.
public class CustomUserDetailsService implements UserDetailsService {
//UserDetailsService--To allow Spring Security to load user credentials and roles from a custom data source like a database
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword()) // BCrypt hash from DB
                .roles(user.getRole())
                .build();
    }
}

