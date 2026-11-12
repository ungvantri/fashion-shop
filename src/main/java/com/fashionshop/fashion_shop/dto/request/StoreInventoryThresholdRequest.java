package com.fashionshop.fashion_shop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreInventoryThresholdRequest {

    @NotNull(message = "storeId không được để trống")
    private Long storeId;

    @NotNull(message = "productVariantId không được để trống")
    private Long productVariantId;

    @NotNull(message = "minThreshold không được để trống")
    @Min(value = 0, message = "minThreshold phải lớn hơn hoặc bằng 0")
    private Integer minThreshold;
}
