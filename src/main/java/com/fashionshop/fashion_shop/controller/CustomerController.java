package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.CustomerRequest;
import com.fashionshop.fashion_shop.dto.request.CustomerTierRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.CustomerResponse;
import com.fashionshop.fashion_shop.dto.response.CustomerTierResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.service.CustomerService;
import com.fashionshop.fashion_shop.service.CustomerTierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerTierService customerTierService;

    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<PageResponse<CustomerResponse>>> getCustomers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long tierId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        PageResponse<CustomerResponse> pageResponse = customerService.getCustomers(search, tierId, page, size);
        ApiResponse<PageResponse<CustomerResponse>> result = ApiResponse.<PageResponse<CustomerResponse>>builder()
                .success(true)
                .message("Lấy danh sách khách hàng thành công")
                .data(pageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/customers/phone/{phone}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByPhone(@PathVariable String phone) {
        CustomerResponse response = customerService.getCustomerByPhone(phone);
        ApiResponse<CustomerResponse> result = ApiResponse.<CustomerResponse>builder()
                .success(true)
                .message("Tra cứu thông tin khách hàng thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/customers")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest request) {
        CustomerResponse response = customerService.createCustomer(request);
        ApiResponse<CustomerResponse> result = ApiResponse.<CustomerResponse>builder()
                .success(true)
                .message("Tạo khách hàng mới thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/customer-tiers")
    public ResponseEntity<ApiResponse<List<CustomerTierResponse>>> getAllTiers() {
        List<CustomerTierResponse> response = customerTierService.getAllTiers();
        ApiResponse<List<CustomerTierResponse>> result = ApiResponse.<List<CustomerTierResponse>>builder()
                .success(true)
                .message("Lấy danh sách hạng thành viên thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/customer-tiers")
    public ResponseEntity<ApiResponse<CustomerTierResponse>> createTier(@Valid @RequestBody CustomerTierRequest request) {
        CustomerTierResponse response = customerTierService.createTier(request);
        ApiResponse<CustomerTierResponse> result = ApiResponse.<CustomerTierResponse>builder()
                .success(true)
                .message("Tạo chính sách hạng thành viên thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
