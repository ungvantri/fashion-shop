package com.fashionshop.fashion_shop.dto.response;

import com.fashionshop.fashion_shop.enums.RoleType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private Long storeId;
    private String storeName;
    private RoleType role;
    private String username;
    private String fullName;
    private String phone;
    private BigDecimal commissionRate;
    private Boolean isActive;
}
