package com.fashionshop.fashion_shop.dto.response;

import com.fashionshop.fashion_shop.enums.OrderChannel;
import com.fashionshop.fashion_shop.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private String orderCode;
    private Long storeId;
    private String storeName;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private Long shiftId;
    private Long cashierId;
    private String cashierName;
    private Long sellerId;
    private String sellerName;
    private OrderChannel channel;
    private BigDecimal totalItemAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
    private List<OrderPaymentResponse> payments;
}
