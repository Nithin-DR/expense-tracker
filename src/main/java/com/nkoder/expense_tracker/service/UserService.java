package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.model.User;
import com.nkoder.expense_tracker.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void register(String username, String password, String role) {

        User user = new User();
        user.setUsername(username);

        // BCrypt hashing happens HERE
        user.setPassword(passwordEncoder.encode(password));

        user.setRole(role);

        userRepository.save(user);
    }
}


