package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.StockTransfer;
import com.fashionshop.fashion_shop.enums.TransferStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {

    @Query(value = "SELECT st FROM StockTransfer st " +
            "JOIN FETCH st.fromStore fs " +
            "JOIN FETCH st.toStore ts " +
            "LEFT JOIN FETCH st.sentByUser su " +
            "LEFT JOIN FETCH st.receivedByUser ru " +
            "WHERE (:fromStoreId IS NULL OR fs.id = :fromStoreId) " +
            "AND (:toStoreId IS NULL OR ts.id = :toStoreId) " +
            "AND (:status IS NULL OR st.status = :status)",
            countQuery = "SELECT COUNT(st) FROM StockTransfer st " +
            "WHERE (:fromStoreId IS NULL OR st.fromStore.id = :fromStoreId) " +
            "AND (:toStoreId IS NULL OR st.toStore.id = :toStoreId) " +
            "AND (:status IS NULL OR st.status = :status)")
    Page<StockTransfer> findStockTransfers(
            @Param("fromStoreId") Long fromStoreId,
            @Param("toStoreId") Long toStoreId,
            @Param("status") TransferStatus status,
            Pageable pageable
    );
}
