package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.StoreInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreInventoryRepository extends JpaRepository<StoreInventory, Long> {

    @Query(value = "SELECT si FROM StoreInventory si " +
            "JOIN FETCH si.store s " +
            "JOIN FETCH si.productVariant pv " +
            "JOIN FETCH pv.product p " +
            "WHERE (:storeId IS NULL OR s.id = :storeId) " +
            "AND (:search IS NULL OR (" +
            "  LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "  LOWER(pv.sku) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "  pv.barcode LIKE CONCAT('%', :search, '%')" +
            "))",
            countQuery = "SELECT COUNT(si) FROM StoreInventory si " +
            "WHERE (:storeId IS NULL OR si.store.id = :storeId) " +
            "AND (:search IS NULL OR (" +
            "  LOWER(si.productVariant.product.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "  LOWER(si.productVariant.sku) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "  si.productVariant.barcode LIKE CONCAT('%', :search, '%')" +
            "))")
    Page<StoreInventory> findInventories(@Param("storeId") Long storeId, @Param("search") String search, Pageable pageable);

    @Query("SELECT si FROM StoreInventory si " +
            "JOIN FETCH si.store s " +
            "JOIN FETCH si.productVariant pv " +
            "JOIN FETCH pv.product p " +
            "WHERE (:storeId IS NULL OR s.id = :storeId) " +
            "AND si.quantity <= si.minThreshold " +
            "ORDER BY si.quantity ASC")
    List<StoreInventory> findLowStockInventories(@Param("storeId") Long storeId);

    Optional<StoreInventory> findByStoreIdAndProductVariantId(Long storeId, Long productVariantId);
}
