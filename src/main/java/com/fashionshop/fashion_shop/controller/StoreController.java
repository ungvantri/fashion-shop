package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.StoreRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.StoreResponse;
import com.fashionshop.fashion_shop.service.StoreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getAllStores() {
        List<StoreResponse> storeResponse = storeService.getAllStores();
        ApiResponse<List<StoreResponse>> result = ApiResponse.<List<StoreResponse>>builder()
                .success(true)
                .message("Lấy danh sách toàn bộ các chi nhánh thành công")
                .data(storeResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreById(@PathVariable Long id) {
        StoreResponse storeResponse = storeService.getStoreById(id);
        ApiResponse<StoreResponse> result = ApiResponse.<StoreResponse>builder()
                .success(true)
                .message("Lấy thông tin chi nhánh thành công")
                .data(storeResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(@Valid @RequestBody StoreRequest storeRequest) {
        StoreResponse storeResponse = storeService.createStore(storeRequest);
        ApiResponse<StoreResponse> result = ApiResponse.<StoreResponse>builder()
                .success(true)
                .message("Tạo chi nhánh thành công")
                .data(storeResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreResponse>> updateStore(@PathVariable Long id, @Valid @RequestBody StoreRequest storeRequest) {
        StoreResponse storeResponse = storeService.updateStore(id, storeRequest);
        ApiResponse<StoreResponse> result = ApiResponse.<StoreResponse>builder()
                .success(true)
                .message("Cập nhật chi nhánh thành công")
                .data(storeResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}
