package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.VoucherRequest;
import com.fashionshop.fashion_shop.dto.request.VoucherValidationRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.VoucherResponse;
import com.fashionshop.fashion_shop.dto.response.VoucherValidationResponse;
import com.fashionshop.fashion_shop.service.VoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<VoucherResponse>>> getVouchers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        PageResponse<VoucherResponse> pageResponse = voucherService.getVouchers(search, page, size);
        ApiResponse<PageResponse<VoucherResponse>> result = ApiResponse.<PageResponse<VoucherResponse>>builder()
                .success(true)
                .message("Lấy danh sách mã giảm giá thành công")
                .data(pageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VoucherResponse>> createVoucher(@Valid @RequestBody VoucherRequest request) {
        VoucherResponse response = voucherService.createVoucher(request);
        ApiResponse<VoucherResponse> result = ApiResponse.<VoucherResponse>builder()
                .success(true)
                .message("Tạo mã giảm giá mới thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<VoucherValidationResponse>> validateVoucher(
            @Valid @RequestBody VoucherValidationRequest request
    ) {
        VoucherValidationResponse response = voucherService.validateVoucher(request);
        ApiResponse<VoucherValidationResponse> result = ApiResponse.<VoucherValidationResponse>builder()
                .success(response.getIsValid())
                .message(response.getMessage())
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
