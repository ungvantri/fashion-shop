package com.fashionshop.fashion_shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {
    @NotNull(message = "Sản phẩm biến thể không được để trống")
    private Long productVariantId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng tối thiểu là 1")
    private Integer quantity;

    @NotNull(message = "Giá bán không được để trống")
    @Positive(message = "Giá bán phải lớn hơn 0")
    private BigDecimal retailPrice;

    @PositiveOrZero(message = "Số tiền giảm giá phải lớn hơn hoặc bằng 0")
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;
}
