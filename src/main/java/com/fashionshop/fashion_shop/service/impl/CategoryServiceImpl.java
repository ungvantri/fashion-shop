package com.fashionshop.fashion_shop.service.impl;

import com.fashionshop.fashion_shop.dto.request.CategoryRequest;
import com.fashionshop.fashion_shop.dto.response.CategoryResponse;
import com.fashionshop.fashion_shop.entity.Category;
import com.fashionshop.fashion_shop.exception.ResourceNotFoundException;
import com.fashionshop.fashion_shop.mapper.CategoryMapper;
import com.fashionshop.fashion_shop.repository.CategoryRepository;
import com.fashionshop.fashion_shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toListDto(categories);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toEntity(categoryRequest);
        if (categoryRequest.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryRequest.getParentId()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục cha có id: " + categoryRequest.getParentId()));
            category.setParent(parent);
        }
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }
}
