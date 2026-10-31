package com.fashionshop.fashion_shop.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {
    @NotBlank(message = "Mã sản phẩm không được để trống")
    @Size(max = 50, message = "Mã sản phẩm tối đa 50 ký tự")
    private String code;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm tối đa 255 ký tự")
    private String name;

    @NotNull(message = "Danh mục không được để trống")
    private Long categoryId;

    private Long brandId;

    @Size(max = 100, message = "Mùa tối đa 100 ký tự")
    private String season;

    @Builder.Default
    private Boolean isActive = true;

    private String description;

    @NotEmpty(message = "Sản phẩm phải có ít nhất một biến thể")
    @Valid
    private List<ProductVariantRequest> variants;
}
