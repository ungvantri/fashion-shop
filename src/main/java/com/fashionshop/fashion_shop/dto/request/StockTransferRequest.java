package com.fashionshop.fashion_shop.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransferRequest {
    @NotNull(message = "Chi nhánh chuyển đi không được để trống")
    private Long fromStoreId;

    @NotNull(message = "Chi nhánh nhận không được để trống")
    private Long toStoreId;

    private Long sentByUserId;

    private String note;

    @NotEmpty(message = "Phiếu chuyển kho phải có ít nhất một sản phẩm")
    @Valid
    private List<StockTransferItemRequest> items;
}
