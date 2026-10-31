package com.fashionshop.fashion_shop.dto.response;

import com.fashionshop.fashion_shop.enums.ShiftStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkShiftResponse {
    private Long id;
    private Long storeId;
    private String storeName;
    private Long userId;
    private String userName;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private BigDecimal initialCash;
    private BigDecimal systemCash;
    private BigDecimal actualCash;
    private BigDecimal difference;
    private ShiftStatus status;
}
