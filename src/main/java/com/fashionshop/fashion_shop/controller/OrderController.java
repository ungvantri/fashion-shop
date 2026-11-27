package com.fashionshop.fashion_shop.controller;

import com.fashionshop.fashion_shop.dto.request.OrderCreateRequest;
import com.fashionshop.fashion_shop.dto.request.ReturnOrderRequest;
import com.fashionshop.fashion_shop.dto.response.ApiResponse;
import com.fashionshop.fashion_shop.dto.response.OrderResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.ReturnOrderResponse;
import com.fashionshop.fashion_shop.enums.OrderChannel;
import com.fashionshop.fashion_shop.enums.OrderStatus;
import com.fashionshop.fashion_shop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        OrderResponse response = orderService.createOrder(request);
        ApiResponse<OrderResponse> result = ApiResponse.<OrderResponse>builder()
                .success(true)
                .message("Tạo hóa đơn bán hàng thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getOrders(
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) OrderChannel channel,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        PageResponse<OrderResponse> pageResponse = orderService.getOrders(storeId, channel, status, fromDate, toDate, page, size);
        ApiResponse<PageResponse<OrderResponse>> result = ApiResponse.<PageResponse<OrderResponse>>builder()
                .success(true)
                .message("Lấy danh sách đơn hàng thành công")
                .data(pageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/orders/{orderCode}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByCode(@PathVariable String orderCode) {
        OrderResponse response = orderService.getOrderByCode(orderCode);
        ApiResponse<OrderResponse> result = ApiResponse.<OrderResponse>builder()
                .success(true)
                .message("Lấy thông tin chi tiết đơn hàng thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/orders/{orderCode}/send-receipt")
    public ResponseEntity<ApiResponse<Void>> sendReceipt(
            @PathVariable String orderCode,
            @RequestBody(required = false) Map<String, String> body
    ) {
        String email = body != null ? body.get("email") : null;
        orderService.sendReceipt(orderCode, email);
        ApiResponse<Void> result = ApiResponse.<Void>builder()
                .success(true)
                .message("Gửi hóa đơn điện tử thành công")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/returns")
    public ResponseEntity<ApiResponse<ReturnOrderResponse>> createReturn(@Valid @RequestBody ReturnOrderRequest request) {
        ReturnOrderResponse response = orderService.createReturn(request);
        ApiResponse<ReturnOrderResponse> result = ApiResponse.<ReturnOrderResponse>builder()
                .success(true)
                .message("Tạo phiếu đổi trả hàng thành công")
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/returns")
    public ResponseEntity<ApiResponse<PageResponse<ReturnOrderResponse>>> getReturns(
            @RequestParam(required = false) Long storeId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size
    ) {
        PageResponse<ReturnOrderResponse> pageResponse = orderService.getReturns(storeId, page, size);
        ApiResponse<PageResponse<ReturnOrderResponse>> result = ApiResponse.<PageResponse<ReturnOrderResponse>>builder()
                .success(true)
                .message("Lấy danh sách phiếu đổi trả hàng thành công")
                .data(pageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
