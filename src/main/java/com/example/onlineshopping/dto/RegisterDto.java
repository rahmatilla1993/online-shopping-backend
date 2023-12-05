package com.example.onlineshopping.dto;

import com.example.onlineshopping.annotations.PasswordMatches;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class RegisterDto {

    @NotEmpty(message = "Firstname should not be empty")
    private String firstName;

    @NotEmpty(message = "Lastname should not be empty")
    private String lastName;

    @NotEmpty(message = "Username should not be empty")
    private String username;

    @NotEmpty(message = "Password should not be empty")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).{4,}$",
            message = "Length is at least 4 characters, consists of letters and numbers"
    )
    private String password;

    private String confirmPassword;
}
