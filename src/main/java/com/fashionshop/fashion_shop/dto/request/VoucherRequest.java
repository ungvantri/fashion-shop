package com.fashionshop.fashion_shop.dto.request;

import com.fashionshop.fashion_shop.enums.DiscountType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherRequest {
    @NotBlank(message = "Mã voucher không được để trống")
    @Size(max = 50, message = "Mã voucher tối đa 50 ký tự")
    private String code;

    @NotNull(message = "Loại giảm giá không được để trống")
    private DiscountType discountType;

    @NotNull(message = "Giá trị giảm không được để trống")
    @Positive(message = "Giá trị giảm giá phải lớn hơn 0")
    private BigDecimal discountValue;

    @PositiveOrZero(message = "Giá trị đơn hàng tối thiểu phải lớn hơn hoặc bằng 0")
    private BigDecimal minOrderAmount;

    @Min(value = 1, message = "Lượt sử dụng tối thiểu là 1")
    private Integer maxUses;

    private LocalDate validFrom;

    private LocalDate validTo;
}
