package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.ProductRequest;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.ProductImportResponse;
import com.fashionshop.fashion_shop.dto.response.ProductResponse;
import jakarta.validation.Valid;

public interface ProductService {
    PageResponse<ProductResponse> getProducts(String search, Long categoryId, String season, Boolean isActive, Integer page, Integer size);

    ProductResponse createProduct(@Valid ProductRequest productRequest);

    ProductResponse getProductById(Long id);

    ProductResponse updateProduct(Long id, @Valid ProductRequest productRequest);

    ProductResponse updateStatusProduct(Long id, Boolean isActive);

    ProductImportResponse importProductsFromS3(String fileKey);
}
