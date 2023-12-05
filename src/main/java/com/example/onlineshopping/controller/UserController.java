package com.example.onlineshopping.controller;

import com.example.onlineshopping.facade.UserFacade;
import com.example.onlineshopping.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final AuthService authService;

    @Autowired
    public UserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public HttpEntity<String> user() {
        return ResponseEntity.ok("User Page");
    }

    @GetMapping("/me")
    public HttpEntity<UserFacade> getUser() {
        return ResponseEntity.ok(authService.getUser());
    }
}
