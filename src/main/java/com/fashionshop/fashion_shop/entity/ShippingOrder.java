package com.fashionshop.fashion_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "shipping_orders")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ShippingOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(nullable = false, length = 50)
    private String carrier;

    @Column(nullable = false, unique = true, length = 100)
    private String trackingCode;

    @Column(precision = 10, scale = 2)
    private BigDecimal shippingFee;

    @Column(precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal codAmount = BigDecimal.ZERO;

    @Column(length = 50)
    private String status;
}
