package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponse {
    private Long id;
    private String code;
    private String name;
    private String phone;
    private String address;
    private Boolean isActive;
}
