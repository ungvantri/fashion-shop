package com.fashionshop.fashion_shop.dto.request;

import com.fashionshop.fashion_shop.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPaymentRequest {
    @NotNull(message = "Phương thức thanh toán không được để trống")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Số tiền thanh toán không được để trống")
    @Positive(message = "Số tiền thanh toán phải lớn hơn 0")
    private BigDecimal amount;

    @Size(max = 100, message = "Mã tham chiếu tối đa 100 ký tự")
    private String transactionReference;
}
