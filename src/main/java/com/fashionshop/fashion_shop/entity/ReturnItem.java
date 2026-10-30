package com.fashionshop.fashion_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "return_items")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReturnItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "return_id", nullable = false)
    private ReturnOrder returnOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;

    private String returnReason;
}
