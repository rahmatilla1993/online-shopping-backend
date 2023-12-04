package com.example.onlineshopping.dto;

import jakarta.validation.constraints.Min;
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
public class ProductDto {

    @NotEmpty(message = "Name should be not empty")
    private String name;

    @Min(value = 0, message = "Should be not negative")
    private float price;

    @NotEmpty(message = "Description should be not empty")
    private String description;

    @NotEmpty(message = "Validate date should be not empty")
    @Pattern(
            regexp = "(^0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4}$)",
            message = "Date must match the 'dd-MM-yyyy' template"
    )
    private String validateDate;

    private int categoryId;
    private int brandId;
}
