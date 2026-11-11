package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.ProductVariantResponse;
import com.fashionshop.fashion_shop.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VariantController {
    @Autowired
    private VariantService variantService;

    @GetMapping("/variants/barcode/{barcode}")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> getVariantByBarcode(@PathVariable String barcode) {
        ProductVariantResponse productVariantResponse = variantService.getVariantByBarcode(barcode);
        ApiResponse<ProductVariantResponse> result = ApiResponse.<ProductVariantResponse>builder()
                .success(true)
                .message("Tra cứu mã vạch thành công")
                .data(productVariantResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}
