package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.BrandRequest;
import com.fashionshop.fashion_shop.dto.response.BrandResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface BrandService {
    List<BrandResponse> getAllBrands();

    BrandResponse createBrand(@Valid BrandRequest brandRequest);
}
