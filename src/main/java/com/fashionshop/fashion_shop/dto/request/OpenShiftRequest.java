package com.fashionshop.fashion_shop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenShiftRequest {
    @NotNull(message = "Chi nhánh không được để trống")
    private Long storeId;

    @NotNull(message = "Nhân viên trực ca không được để trống")
    private Long userId;

    @NotNull(message = "Tiền mặt đầu ca không được để trống")
    @PositiveOrZero(message = "Tiền đầu ca phải lớn hơn hoặc bằng 0")
    private BigDecimal initialCash;
}
