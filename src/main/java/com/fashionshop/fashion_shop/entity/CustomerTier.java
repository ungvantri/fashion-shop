package com.fashionshop.fashion_shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "customer_tiers")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CustomerTier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String tierName;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal minSpent;

    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal discountPercent;
}
