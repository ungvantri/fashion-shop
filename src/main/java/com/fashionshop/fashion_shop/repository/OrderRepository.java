package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.Order;
import com.fashionshop.fashion_shop.enums.OrderChannel;
import com.fashionshop.fashion_shop.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o FROM Order o " +
            "JOIN FETCH o.store s " +
            "LEFT JOIN FETCH o.customer c " +
            "JOIN FETCH o.cashier ca " +
            "LEFT JOIN FETCH o.seller se " +
            "WHERE (:storeId IS NULL OR s.id = :storeId) " +
            "AND (:channel IS NULL OR o.channel = :channel) " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:fromDate IS NULL OR o.createdAt >= :fromDate) " +
            "AND (:toDate IS NULL OR o.createdAt <= :toDate)",
            countQuery = "SELECT COUNT(o) FROM Order o " +
            "WHERE (:storeId IS NULL OR o.store.id = :storeId) " +
            "AND (:channel IS NULL OR o.channel = :channel) " +
            "AND (:status IS NULL OR o.status = :status) " +
            "AND (:fromDate IS NULL OR o.createdAt >= :fromDate) " +
            "AND (:toDate IS NULL OR o.createdAt <= :toDate)")
    Page<Order> findOrders(
            @Param("storeId") Long storeId,
            @Param("channel") OrderChannel channel,
            @Param("status") OrderStatus status,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable
    );

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.store s " +
            "LEFT JOIN FETCH o.customer c " +
            "JOIN FETCH o.cashier ca " +
            "LEFT JOIN FETCH o.seller se " +
            "LEFT JOIN FETCH o.items item " +
            "LEFT JOIN FETCH item.productVariant pv " +
            "LEFT JOIN FETCH pv.product p " +
            "WHERE o.orderCode = :orderCode")
    Optional<Order> findByOrderCodeWithDetails(@Param("orderCode") String orderCode);

    Optional<Order> findByOrderCode(String orderCode);

    boolean existsByOrderCode(String orderCode);
}
