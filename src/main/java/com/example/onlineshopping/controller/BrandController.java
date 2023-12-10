package com.example.onlineshopping.controller;

import com.example.onlineshopping.dto.BrandDto;
import com.example.onlineshopping.entity.Brand;
import com.example.onlineshopping.service.BrandService;
import com.example.onlineshopping.validation.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brand")
@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
public class BrandController {

    private final BrandService brandService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public BrandController(BrandService brandService,
                           ModelMapper modelMapper,
                           ResponseErrorValidation responseErrorValidation
    ) {
        this.brandService = brandService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll() {
        List<Brand> brandList = brandService.getAll();
        return ResponseEntity.ok(getBrandFacades(brandList));
    }

    @PostMapping("/add")
    public HttpEntity<?> add(@RequestBody @Valid BrandDto brandDto,
                             BindingResult bindingResult) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(brandService.save(brandDto));
    }

    private List<BrandDto> getBrandFacades(List<Brand> brands) {
        return brands
                .stream()
                .map(brand -> modelMapper.map(brand, BrandDto.class))
                .toList();
    }
}
