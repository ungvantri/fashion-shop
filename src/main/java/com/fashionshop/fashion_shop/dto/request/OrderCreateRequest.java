package com.fashionshop.fashion_shop.dto.request;

import com.fashionshop.fashion_shop.enums.OrderChannel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequest {
    @NotNull(message = "Chi nhánh không được để trống")
    private Long storeId;

    private Long customerId;

    private Long shiftId;

    @NotNull(message = "Thu ngân không được để trống")
    private Long cashierId;

    private Long sellerId;

    @NotNull(message = "Kênh bán không được để trống")
    private OrderChannel channel;

    private String voucherCode;

    @PositiveOrZero(message = "Số tiền giảm giá đơn hàng phải lớn hơn hoặc bằng 0")
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotEmpty(message = "Đơn hàng phải có ít nhất một sản phẩm")
    @Valid
    private List<OrderItemRequest> items;

    @NotEmpty(message = "Đơn hàng phải có ít nhất một phương thức thanh toán")
    @Valid
    private List<OrderPaymentRequest> payments;
}
