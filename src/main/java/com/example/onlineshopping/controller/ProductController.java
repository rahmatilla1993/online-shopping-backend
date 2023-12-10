package com.example.onlineshopping.controller;

import com.example.onlineshopping.dto.ProductDto;
import com.example.onlineshopping.entity.Product;
import com.example.onlineshopping.exception.ObjectNotFoundException;
import com.example.onlineshopping.facade.ProductFacade;
import com.example.onlineshopping.response.ApiResponse;
import com.example.onlineshopping.service.ProductService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public ProductController(ProductService productService,
                             ModelMapper modelMapper,
                             ResponseErrorValidation responseErrorValidation
    ) {
        this.productService = productService;
        this.modelMapper = modelMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll() {
        List<Product> productList = productService.getAll();
        List<ProductFacade> list = productList
                .stream()
                .map(this::getProductFacade)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable("id") int id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(getProductFacade(product));
    }

    @PostMapping("/add")
    public HttpEntity<?> save(
            @RequestPart(value = "file", required = false) MultipartFile multipartFile,
            @RequestPart("data") @Valid ProductDto productDto,
            BindingResult bindingResult
    ) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.save(productDto, multipartFile));
    }

    @PutMapping("/edit/{id}")
    public HttpEntity<?> edit(@RequestBody @Valid ProductDto productDto,
                              BindingResult bindingResult,
                              @PathVariable("id") int id) {
        var errors = responseErrorValidation.mapValidationResult(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        return ResponseEntity.ok(productService.edit(productDto, id));
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable("id") int id) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(productService.delete(id));
    }

    private ProductFacade getProductFacade(Product product) {
        ProductFacade productFacade = modelMapper.map(product, ProductFacade.class);
        productFacade.setValidateDate(product.getValidateDate().toString());
        productFacade.setCategoryName(product.getCategory().getName());
        productFacade.setBrandName(product.getBrand().getName());
        return productFacade;
    }

    @ExceptionHandler
    public HttpEntity<ApiResponse> handleExistsException(ObjectNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
    }
}
