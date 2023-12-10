package com.example.onlineshopping.facade;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductFacade {
    private String name;
    private String description;
    private List<String> imageUrls;
    private float price;
    private String validateDate;
    private String categoryName;
    private String brandName;
}
