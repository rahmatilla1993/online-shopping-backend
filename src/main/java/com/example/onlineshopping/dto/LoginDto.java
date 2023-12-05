package com.example.onlineshopping.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginDto {

    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).{4,}$",
            message = "Length is at least 4 characters, consists of letters and numbers"
    )
    private String password;
}
