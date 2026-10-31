package com.fashionshop.fashion_shop.dto.response;

import com.fashionshop.fashion_shop.enums.TransferStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransferResponse {
    private Long id;
    private String code;
    private Long fromStoreId;
    private String fromStoreName;
    private Long toStoreId;
    private String toStoreName;
    private TransferStatus status;
    private Long sentByUserId;
    private String sentByUserName;
    private Long receivedByUserId;
    private String receivedByUserName;
    private LocalDateTime shippedAt;
    private LocalDateTime receivedAt;
    private String note;
    private List<StockTransferItemResponse> items;
}
