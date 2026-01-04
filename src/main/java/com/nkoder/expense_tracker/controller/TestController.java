package com.nkoder.expense_tracker.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Public Page";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String user() {
        return "User Page";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin Page";
    }

    @GetMapping("/thread")
    public String threadInfo() {
        return "Thread name: " + Thread.currentThread().getName();
    }
}


