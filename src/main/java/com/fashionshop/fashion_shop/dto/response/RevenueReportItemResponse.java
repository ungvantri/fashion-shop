package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueReportItemResponse {
    private String groupKey;
    private Long orderCount;
    private BigDecimal revenue;
}
