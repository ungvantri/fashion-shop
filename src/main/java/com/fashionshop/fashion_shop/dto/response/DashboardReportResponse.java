package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardReportResponse {
    private BigDecimal todayRevenue;
    private Long todayOrderCount;
    private Long newCustomerCount;
    private Long lowStockItemCount;
}
