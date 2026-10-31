package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnOrderResponse {
    private Long id;
    private String returnCode;
    private Long originalOrderId;
    private String originalOrderCode;
    private Long storeId;
    private String storeName;
    private Long createdById;
    private String createdByName;
    private BigDecimal refundAmount;
    private String status;
    private LocalDateTime createdAt;
    private List<ReturnItemResponse> items;
}
