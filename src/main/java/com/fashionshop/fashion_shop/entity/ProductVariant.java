package com.fashionshop.fashion_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "product_variants",
        indexes = {
                // Đánh Index barcode để quét tại POS < 2s
                @Index(name = "idx_variant_barcode", columnList = "barcode", unique = true)
        }
)
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @Column(nullable = false, length = 100)
    private String barcode;

    @Column(length = 20, nullable = false)
    private String size;

    @Column(length = 50, nullable = false)
    private String color;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal costPrice;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal retailPrice;

    @Column(length = 500)
    private String imageUrl;
}
