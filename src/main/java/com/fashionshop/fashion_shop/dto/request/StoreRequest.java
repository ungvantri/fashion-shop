package com.fashionshop.fashion_shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequest {
    @NotBlank(message = "Mã chi nhánh không được để trống")
    @Size(max = 50, message = "Mã chi nhánh tối đa 50 ký tự")
    private String code;

    @NotBlank(message = "Tên chi nhánh không được để trống")
    @Size(max = 255, message = "Tên chi nhánh tối đa 255 ký tự")
    private String name;

    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    private String address;

    @Builder.Default
    private Boolean isActive = true;
}
