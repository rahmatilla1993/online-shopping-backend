package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.CategoryDto;
import com.example.onlineshopping.entity.Category;
import com.example.onlineshopping.repository.CategoryRepository;
import com.example.onlineshopping.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(int id) {
        return categoryRepository
                .findById(id)
                .orElse(null);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    public ApiResponse save(CategoryDto categoryDto) {
        int categoryId = categoryDto.getParentCategoryId();
        Category parentCategory = findById(categoryId);
        Category category = new Category();
        category.setParentCategory(parentCategory);
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return new ApiResponse("Category Added", true);
    }
}
