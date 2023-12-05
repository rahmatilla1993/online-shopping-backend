package com.example.onlineshopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserExistException extends RuntimeException {
    public UserExistException(String message) {
        super(message);
    }
}