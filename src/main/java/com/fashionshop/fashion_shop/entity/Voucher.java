package com.fashionshop.fashion_shop.entity;

import com.fashionshop.fashion_shop.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "vouchers")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private DiscountType discountType;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal discountValue;

    @Column(precision = 12, scale = 2)
    private BigDecimal minOrderAmount;

    private Integer maxUses;

    @Builder.Default
    private Integer usedCount = 0;

    private LocalDate validFrom;

    private LocalDate validTo;
}
