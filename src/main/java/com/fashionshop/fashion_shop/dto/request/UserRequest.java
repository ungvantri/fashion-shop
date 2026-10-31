package com.fashionshop.fashion_shop.dto.request;

import com.fashionshop.fashion_shop.enums.RoleType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private Long storeId;

    @NotNull(message = "Vai trò không được để trống")
    private RoleType role;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập từ 3 đến 50 ký tự")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    private String password;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 150, message = "Họ tên tối đa 150 ký tự")
    private String fullName;

    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @DecimalMin(value = "0.0", message = "Tỷ lệ hoa hồng không thể âm")
    @DecimalMax(value = "100.0", message = "Tỷ lệ hoa hồng tối đa 100%")
    @Builder.Default
    private BigDecimal commissionRate = BigDecimal.ZERO;

    @Builder.Default
    private Boolean isActive = true;
}
