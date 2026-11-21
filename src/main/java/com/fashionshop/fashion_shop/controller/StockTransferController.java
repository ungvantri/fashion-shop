package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.StockTransferRequest;
import com.fashionshop.fashion_shop.dto.request.StockTransferStatusRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.StockTransferResponse;
import com.fashionshop.fashion_shop.enums.TransferStatus;
import com.fashionshop.fashion_shop.service.StockTransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-transfers")
public class StockTransferController {

    @Autowired
    private StockTransferService stockTransferService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StockTransferResponse>>> getStockTransfers(
            @RequestParam(required = false) Long fromStoreId,
            @RequestParam(required = false) Long toStoreId,
            @RequestParam(required = false) TransferStatus status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        PageResponse<StockTransferResponse> pageResponse = stockTransferService.getStockTransfers(
                fromStoreId, toStoreId, status, page, size
        );
        ApiResponse<PageResponse<StockTransferResponse>> result = ApiResponse.<PageResponse<StockTransferResponse>>builder()
                .success(true)
                .message("Lấy danh sách phiếu chuyển kho thành công")
                .data(pageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StockTransferResponse>> getStockTransferById(@PathVariable Long id) {
        StockTransferResponse response = stockTransferService.getStockTransferById(id);
        ApiResponse<StockTransferResponse> result = ApiResponse.<StockTransferResponse>builder()
                .success(true)
                .message("Lấy chi tiết phiếu chuyển kho thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StockTransferResponse>> createStockTransfer(
            @Valid @RequestBody StockTransferRequest request
    ) {
        StockTransferResponse response = stockTransferService.createStockTransfer(request);
        ApiResponse<StockTransferResponse> result = ApiResponse.<StockTransferResponse>builder()
                .success(true)
                .message("Tạo phiếu chuyển kho thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<StockTransferResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StockTransferStatusRequest request
    ) {
        StockTransferResponse response = stockTransferService.updateStatus(id, request);
        ApiResponse<StockTransferResponse> result = ApiResponse.<StockTransferResponse>builder()
                .success(true)
                .message("Cập nhật trạng thái phiếu chuyển kho và đồng bộ tồn kho thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
