package com.fashionshop.fashion_shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_transfer_items")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class StockTransferItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    private StockTransfer transfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @Column(nullable = false)
    private Integer quantity;
}
