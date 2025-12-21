package com.nkoder.expense_tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String home() {
        return "Public Page";
    }

    @GetMapping("/user")
    public String user() {
        return "User Page";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin Page";
    }
}

