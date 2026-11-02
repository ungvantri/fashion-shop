package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.CategoryRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.CategoryResponse;
import com.fashionshop.fashion_shop.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categoryResponses = categoryService.getAllCategories();
        ApiResponse<List<CategoryResponse>> result = ApiResponse.<List<CategoryResponse>>builder()
                .success(true)
                .message("Lấy danh sách danh mục sản phẩm thành công")
                .data(categoryResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryResponse categoryResponse = categoryService.createCategory(categoryRequest);
        ApiResponse<CategoryResponse> result = ApiResponse.<CategoryResponse>builder()
                .success(true)
                .message("Tạo mới danh mục thành công")
                .data(categoryResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }
}
