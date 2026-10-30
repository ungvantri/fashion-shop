package com.fashionshop.fashion_shop.entity;

import com.fashionshop.fashion_shop.enums.ShiftStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_shifts")
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class WorkShift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime openedAt;

    private LocalDateTime closedAt;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal initialCash;

    @Column(precision = 12, scale = 2)
    private BigDecimal systemCash;

    @Column(precision = 12, scale = 2)
    private BigDecimal actualCash;

    @Column(precision = 12, scale = 2)
    private BigDecimal difference;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ShiftStatus status;
}
