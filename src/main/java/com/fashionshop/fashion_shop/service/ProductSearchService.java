package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.ProductResponse;

import java.math.BigDecimal;

public interface ProductSearchService {
    PageResponse<ProductResponse> searchProducts(
            String keyword,
            Long categoryId,
            Long brandId,
            String color,
            String size,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            int pageIndex,
            Integer pageSize
    );
}
