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
public class CloseShiftRequest {
    @NotNull(message = "Mã ca làm việc không được để trống")
    private Long shiftId;

    @NotNull(message = "Tiền mặt thực tế cuối ca không được để trống")
    @PositiveOrZero(message = "Tiền thực tế phải lớn hơn hoặc bằng 0")
    private BigDecimal actualCash;
}
