package com.fashionshop.fashion_shop.dto.response;

import com.fashionshop.fashion_shop.enums.DiscountType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherResponse {
    private Long id;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private Integer maxUses;
    private Integer usedCount;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Boolean isValid;
}
