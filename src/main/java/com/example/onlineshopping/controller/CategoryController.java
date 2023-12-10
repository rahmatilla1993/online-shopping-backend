package com.example.onlineshopping.controller;

import com.example.onlineshopping.dto.CategoryDto;
import com.example.onlineshopping.entity.Category;
import com.example.onlineshopping.facade.CategoryFacade;
import com.example.onlineshopping.service.CategoryService;
import com.example.onlineshopping.validation.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CategoryController(CategoryService categoryService,
                              ModelMapper modelMapper,
                              ResponseErrorValidation responseErrorValidation
    ) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll() {
        List<Category> categories = categoryService.getAll();
        return ResponseEntity.ok(categoryFacadeList(categories));
    }

    @PostMapping("/add")
    public HttpEntity<?> save(@RequestBody @Valid CategoryDto categoryDto,
                              BindingResult bindingResult) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(categoryService.save(categoryDto));
    }

    private List<CategoryFacade> categoryFacadeList(List<Category> categories) {
        return categories
                .stream()
                .map(category -> {
                    var categoryFacade = modelMapper.map(category, CategoryFacade.class);
                    Category parentCategory = category.getParentCategory();
                    categoryFacade.setParentCategoryName(parentCategory == null ? null : parentCategory.getName());
                    return categoryFacade;
                })
                .toList();
    }
}
