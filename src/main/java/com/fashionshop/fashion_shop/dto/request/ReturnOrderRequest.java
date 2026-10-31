package com.fashionshop.fashion_shop.dto.request;

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
public class ReturnOrderRequest {
    @NotNull(message = "Đơn hàng gốc không được để trống")
    private Long originalOrderId;

    @NotNull(message = "Chi nhánh tiếp nhận không được để trống")
    private Long storeId;

    @NotNull(message = "Nhân viên tiếp nhận không được để trống")
    private Long createdById;

    @PositiveOrZero(message = "Tiền hoàn lại phải lớn hơn hoặc bằng 0")
    @Builder.Default
    private BigDecimal refundAmount = BigDecimal.ZERO;

    @NotEmpty(message = "Phiếu đổi trả phải có ít nhất một sản phẩm")
    @Valid
    private List<ReturnItemRequest> items;
}
