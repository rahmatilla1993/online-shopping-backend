package com.example.onlineshopping.service;

import com.example.onlineshopping.dto.BrandDto;
import com.example.onlineshopping.entity.Brand;
import com.example.onlineshopping.repository.BrandRepository;
import com.example.onlineshopping.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    public Brand findById(int id) {
        return brandRepository
                .findById(id)
                .orElse(null);
    }

    @Transactional
    public ApiResponse save(BrandDto brandDto) {
        Brand brand = new Brand();
        brand.setName(brandDto.getName());
        brandRepository.save(brand);
        return new ApiResponse("Brand added", true);
    }
}
