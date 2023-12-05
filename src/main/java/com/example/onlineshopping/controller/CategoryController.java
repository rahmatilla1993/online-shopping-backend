package com.example.onlineshopping.controller;

import com.example.onlineshopping.dto.CategoryDto;
import com.example.onlineshopping.entity.Category;
import com.example.onlineshopping.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll() {
        List<Category> categories = categoryService.getAll();
        return ResponseEntity.ok(categoryDtoList(categories));
    }

    private List<CategoryDto> categoryDtoList(List<Category> categories) {
        return categories
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();
    }
}
