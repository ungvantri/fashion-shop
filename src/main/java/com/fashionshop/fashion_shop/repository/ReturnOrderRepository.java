package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.ReturnOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReturnOrderRepository extends JpaRepository<ReturnOrder, Long> {

    @Query(value = "SELECT ro FROM ReturnOrder ro " +
            "JOIN FETCH ro.originalOrder oo " +
            "JOIN FETCH ro.store s " +
            "JOIN FETCH ro.createdBy u " +
            "WHERE (:storeId IS NULL OR s.id = :storeId)",
            countQuery = "SELECT COUNT(ro) FROM ReturnOrder ro " +
            "WHERE (:storeId IS NULL OR ro.store.id = :storeId)")
    Page<ReturnOrder> findReturns(@Param("storeId") Long storeId, Pageable pageable);

    @Query("SELECT ro FROM ReturnOrder ro " +
            "JOIN FETCH ro.originalOrder oo " +
            "JOIN FETCH ro.store s " +
            "JOIN FETCH ro.createdBy u " +
            "LEFT JOIN FETCH ro.items item " +
            "LEFT JOIN FETCH item.productVariant pv " +
            "LEFT JOIN FETCH pv.product p " +
            "WHERE ro.id = :id")
    Optional<ReturnOrder> findByIdWithDetails(@Param("id") Long id);

    List<ReturnOrder> findByOriginalOrderId(Long originalOrderId);
}
