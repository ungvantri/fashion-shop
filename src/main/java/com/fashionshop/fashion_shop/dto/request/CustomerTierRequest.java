package com.fashionshop.fashion_shop.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerTierRequest {
    @NotBlank(message = "Tên hạng thành viên không được để trống")
    @Size(max = 50, message = "Tên hạng thành viên tối đa 50 ký tự")
    private String tierName;

    @NotNull(message = "Hạn mức chi tiêu tối thiểu không được để trống")
    @PositiveOrZero(message = "Hạn mức chi tiêu phải lớn hơn hoặc bằng 0")
    private BigDecimal minSpent;

    @NotNull(message = "Tỷ lệ giảm giá không được để trống")
    @DecimalMin(value = "0.0", message = "Tỷ lệ giảm giá tối thiểu 0%")
    @DecimalMax(value = "100.0", message = "Tỷ lệ giảm giá tối đa 100%")
    private BigDecimal discountPercent;
}
