package com.fashionshop.fashion_shop.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantRequest {
    private Long id;

    @NotBlank(message = "Mã SKU không được để trống")
    @Size(max = 100, message = "Mã SKU tối đa 100 ký tự")
    private String sku;

    @NotBlank(message = "Mã vạch (barcode) không được để trống")
    @Size(max = 100, message = "Mã vạch tối đa 100 ký tự")
    private String barcode;

    @NotBlank(message = "Kích thước (size) không được để trống")
    @Size(max = 20, message = "Size tối đa 20 ký tự")
    private String size;

    @NotBlank(message = "Màu sắc không được để trống")
    @Size(max = 50, message = "Màu sắc tối đa 50 ký tự")
    private String color;

    @NotNull(message = "Giá vốn không được để trống")
    @PositiveOrZero(message = "Giá vốn phải lớn hơn hoặc bằng 0")
    private BigDecimal costPrice;

    @NotNull(message = "Giá bán không được để trống")
    @Positive(message = "Giá bán phải lớn hơn 0")
    private BigDecimal retailPrice;

    @Size(max = 500, message = "Đường dẫn hình ảnh tối đa 500 ký tự")
    private String imageUrl;
}
