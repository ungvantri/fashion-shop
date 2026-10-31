package com.fashionshop.fashion_shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReturnItemRequest {
    @NotNull(message = "Sản phẩm đổi trả không được để trống")
    private Long productVariantId;

    @NotNull(message = "Số lượng đổi trả không được để trống")
    @Min(value = 1, message = "Số lượng đổi trả tối thiểu là 1")
    private Integer quantity;

    @Size(max = 255, message = "Lý do đổi trả tối đa 255 ký tự")
    private String returnReason;
}
