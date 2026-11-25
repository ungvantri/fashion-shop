package com.fashionshop.fashion_shop.dto.response;

import com.fashionshop.fashion_shop.enums.DiscountType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherValidationResponse {
    private Boolean isValid;
    private String message;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal calculatedDiscountAmount;
}
