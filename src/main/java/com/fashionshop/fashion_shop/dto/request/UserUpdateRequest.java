package com.fashionshop.fashion_shop.dto.request;

import com.fashionshop.fashion_shop.enums.RoleType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {
    private Long storeId;
    private RoleType role;

    @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    private String password;

    @Size(max = 150, message = "Họ tên tối đa 150 ký tự")
    private String fullName;

    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @DecimalMin(value = "0.0", message = "Tỷ lệ hoa hồng không thể âm")
    @DecimalMax(value = "100.0", message = "Tỷ lệ hoa hồng tối đa 100%")
    private BigDecimal commissionRate;

    private Boolean isActive;
}
