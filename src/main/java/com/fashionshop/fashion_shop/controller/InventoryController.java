package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.StoreInventoryThresholdRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.StoreInventoryResponse;
import com.fashionshop.fashion_shop.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventories")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StoreInventoryResponse>>> getInventories(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        PageResponse<StoreInventoryResponse> pageResponse = inventoryService.getInventories(storeId, search, page, size);
        ApiResponse<PageResponse<StoreInventoryResponse>> result = ApiResponse.<PageResponse<StoreInventoryResponse>>builder()
                .success(true)
                .message("Lấy danh sách tồn kho thành công")
                .data(pageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse<List<StoreInventoryResponse>>> getLowStockInventories(
            @RequestParam(required = false) Long storeId
    ) {
        List<StoreInventoryResponse> lowStockList = inventoryService.getLowStockInventories(storeId);
        ApiResponse<List<StoreInventoryResponse>> result = ApiResponse.<List<StoreInventoryResponse>>builder()
                .success(true)
                .message("Lấy danh sách sản phẩm cảnh báo sắp hết hàng thành công")
                .data(lowStockList)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/threshold")
    public ResponseEntity<ApiResponse<StoreInventoryResponse>> updateThreshold(
            @Valid @RequestBody StoreInventoryThresholdRequest request
    ) {
        StoreInventoryResponse response = inventoryService.updateThreshold(request);
        ApiResponse<StoreInventoryResponse> result = ApiResponse.<StoreInventoryResponse>builder()
                .success(true)
                .message("Cập nhật ngưỡng cảnh báo hết hàng thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
