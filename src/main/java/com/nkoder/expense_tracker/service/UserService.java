package com.nkoder.expense_tracker.service;

import com.nkoder.expense_tracker.exception.DuplicateUserException;
import com.nkoder.expense_tracker.model.User;
import com.nkoder.expense_tracker.repo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nkoder.expense_tracker.model.AuthProvider;


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

        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateUserException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setProvider(AuthProvider.LOCAL);

        userRepository.save(user);
    }


    public User registerOAuthUser(String email) {
        User user = new User();
        user.setUsername(email);
        user.setProvider(AuthProvider.GOOGLE);
        user.setRole("USER");
        return userRepository.save(user);
    }

}


