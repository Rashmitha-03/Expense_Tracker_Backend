package com.expensetracker.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.user.dto.LoginRequest;
import com.expensetracker.user.dto.LoginResponse;
import com.expensetracker.user.dto.RegisterRequest;
import com.expensetracker.user.dto.RegisterResponse;
import com.expensetracker.user.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(
            @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = userService.registerUser(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

}
