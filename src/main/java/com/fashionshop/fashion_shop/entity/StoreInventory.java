package com.fashionshop.fashion_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "store_inventories",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_store_variant", columnNames = {"store_id", "product_variant_id"})
        }
)
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class StoreInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer minThreshold = 5;
}
