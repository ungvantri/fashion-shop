package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerTierResponse {
    private Long id;
    private String tierName;
    private BigDecimal minSpent;
    private BigDecimal discountPercent;
}
