package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.BrandRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.BrandResponse;
import com.fashionshop.fashion_shop.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrands() {
        List<BrandResponse> brandResponses = brandService.getAllBrands();
        ApiResponse<List<BrandResponse>> result = ApiResponse.<List<BrandResponse>>builder()
                .success(true)
                .message("Lấy danh sách thương hiệu thời trang thành công")
                .data(brandResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(@Valid @RequestBody BrandRequest brandRequest) {
        BrandResponse brandResponse = brandService.createBrand(brandRequest);
        ApiResponse<BrandResponse> result = ApiResponse.<BrandResponse>builder()
                .success(true)
                .message("Tạo thương hiệu mới thành công")
                .data(brandResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }
}
