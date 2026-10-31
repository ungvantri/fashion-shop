package com.fashionshop.fashion_shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransferItemRequest {
    @NotNull(message = "Sản phẩm biến thể không được để trống")
    private Long productVariantId;

    @NotNull(message = "Số lượng chuyển không được để trống")
    @Min(value = 1, message = "Số lượng chuyển tối thiểu là 1")
    private Integer quantity;
}
