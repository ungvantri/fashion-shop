package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.BrandRequest;
import com.fashionshop.fashion_shop.dto.response.BrandResponse;
import com.fashionshop.fashion_shop.entity.Brand;
import com.fashionshop.fashion_shop.mapper.BrandMapper;
import com.fashionshop.fashion_shop.repository.BrandRepository;
import com.fashionshop.fashion_shop.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandMapper brandMapper;

    @Override
    public List<BrandResponse> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        return brandMapper.toListDto(brands);
    }

    @Override
    @Transactional
    public BrandResponse createBrand(BrandRequest brandRequest) {
        Brand brand = brandMapper.toEntity(brandRequest);
        Brand saved = brandRepository.save(brand);
        return brandMapper.toDto(saved);
    }
}
