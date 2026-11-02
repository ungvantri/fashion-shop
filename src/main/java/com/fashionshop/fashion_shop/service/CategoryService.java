package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.CategoryRequest;
import com.fashionshop.fashion_shop.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    CategoryResponse createCategory(CategoryRequest categoryRequest);
}
