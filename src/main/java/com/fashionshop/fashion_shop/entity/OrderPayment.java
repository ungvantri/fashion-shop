package com.fashionshop.fashion_shop.entity;

import com.fashionshop.fashion_shop.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_payments")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private PaymentMethod paymentMethod;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;
    private String transactionReference;
}
