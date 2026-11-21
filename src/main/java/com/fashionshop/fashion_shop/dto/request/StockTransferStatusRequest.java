package com.fashionshop.fashion_shop.dto.request;

import com.fashionshop.fashion_shop.enums.TransferStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransferStatusRequest {

    @NotNull(message = "Trạng thái không được để trống")
    private TransferStatus status;

    private Long receivedByUserId;
}
