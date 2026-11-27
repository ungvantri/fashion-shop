package com.fashionshop.fashion_shop.service;

import com.fashionshop.fashion_shop.dto.request.OrderCreateRequest;
import com.fashionshop.fashion_shop.dto.request.ReturnOrderRequest;
import com.fashionshop.fashion_shop.dto.response.OrderResponse;
import com.fashionshop.fashion_shop.dto.response.PageResponse;
import com.fashionshop.fashion_shop.dto.response.ReturnOrderResponse;
import com.fashionshop.fashion_shop.enums.OrderChannel;
import com.fashionshop.fashion_shop.enums.OrderStatus;

import java.time.LocalDate;

public interface OrderService {

    OrderResponse createOrder(OrderCreateRequest request);

    PageResponse<OrderResponse> getOrders(
            Long storeId,
            OrderChannel channel,
            OrderStatus status,
            LocalDate fromDate,
            LocalDate toDate,
            Integer page,
            Integer size
    );

    OrderResponse getOrderByCode(String orderCode);

    void sendReceipt(String orderCode, String recipientEmail);

    ReturnOrderResponse createReturn(ReturnOrderRequest request);

    PageResponse<ReturnOrderResponse> getReturns(Long storeId, Integer page, Integer size);
}
