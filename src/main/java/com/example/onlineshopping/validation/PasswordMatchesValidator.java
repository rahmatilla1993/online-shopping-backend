package com.example.onlineshopping.validation;

import com.example.onlineshopping.annotations.PasswordMatches;
import com.example.onlineshopping.dto.RegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterDto> {
    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext context) {
        return registerDto.getPassword().equals(registerDto.getConfirmPassword());
    }
}
