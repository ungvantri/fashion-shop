package com.fashionshop.fashion_shop.dto.response;

import com.fashionshop.fashion_shop.enums.PaymentMethod;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPaymentResponse {
    private Long id;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
    private String transactionReference;
}
