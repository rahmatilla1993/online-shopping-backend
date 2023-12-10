package com.example.onlineshopping.facade;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryFacade {

    private String name;
    private String parentCategoryName;
}
