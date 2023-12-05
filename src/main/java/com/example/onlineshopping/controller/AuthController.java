package com.example.onlineshopping.controller;

import com.example.onlineshopping.dto.LoginDto;
import com.example.onlineshopping.dto.RegisterDto;
import com.example.onlineshopping.exception.UserExistException;
import com.example.onlineshopping.response.ApiResponse;
import com.example.onlineshopping.service.AuthService;
import com.example.onlineshopping.validation.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public AuthController(AuthService authService,
                          ResponseErrorValidation responseErrorValidation
    ) {
        this.authService = authService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/signup")
    public HttpEntity<?> register(
            @RequestBody @Valid RegisterDto registerDto,
            BindingResult bindingResult
    ) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.create(registerDto));
    }

    @PostMapping("/signin")
    public HttpEntity<?> login(
            @RequestBody @Valid LoginDto loginDto,
            BindingResult bindingResult
    ) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }

        return ResponseEntity
                .ok(authService.login(loginDto));
    }

    @ExceptionHandler
    public HttpEntity<ApiResponse> handleExistsException(UserExistException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
    }

    @ExceptionHandler
    public HttpEntity<ApiResponse> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
    }
}
