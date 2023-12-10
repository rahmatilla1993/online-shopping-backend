package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.ProductDto;
import com.example.onlineshopping.entity.Brand;
import com.example.onlineshopping.entity.Category;
import com.example.onlineshopping.entity.Product;
import com.example.onlineshopping.exception.ObjectNotFoundException;
import com.example.onlineshopping.repository.ProductRepository;
import com.example.onlineshopping.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ImageService imageService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public ProductService(ProductRepository productRepository,
                          BrandService brandService,
                          CategoryService categoryService,
                          ImageService imageService
    ) {
        this.productRepository = productRepository;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.imageService = imageService;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product findById(int id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found"));
    }

    @Transactional
    public ApiResponse save(ProductDto productDto, MultipartFile multipartFile) {
        Brand brand = brandService.findById(productDto.getBrandId());
        Category category = categoryService.findById(productDto.getCategoryId());
        Product product = new Product();
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setValidateDate(LocalDate.parse(productDto.getValidateDate(), dateTimeFormatter));
        product.setCategory(category);
        product.setBrand(brand);
        if (multipartFile != null) {
            try {
                String imageUrl = imageService.uploadImage(multipartFile);
                List<String> images = new ArrayList<>();
                images.add(imageUrl);
                product.setImageUrls(images);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        productRepository.save(product);
        return new ApiResponse("Product added", true);
    }

    @Transactional
    public ApiResponse edit(ProductDto productDto, int id) {
        Product editedProduct = findById(id);
        Brand brand = brandService.findById(productDto.getBrandId());
        Category category = categoryService.findById(productDto.getCategoryId());
        editedProduct.setName(productDto.getName());
        editedProduct.setPrice(productDto.getPrice());
        editedProduct.setDescription(productDto.getDescription());
        editedProduct.setValidateDate(LocalDate.parse(productDto.getValidateDate(), dateTimeFormatter));
        editedProduct.setCategory(category);
        editedProduct.setBrand(brand);
        return new ApiResponse("Product edited", true);
    }

    @Transactional
    public ApiResponse delete(int id) {
        Product product = findById(id);
        productRepository.delete(product);
        return new ApiResponse("Product deleted", true);
    }
}
