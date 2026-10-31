package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private Long id;
    private Long tierId;
    private String tierName;
    private BigDecimal discountPercent;
    private String phone;
    private String fullName;
    private LocalDate birthday;
    private BigDecimal totalSpent;
    private Integer currentPoints;
}
