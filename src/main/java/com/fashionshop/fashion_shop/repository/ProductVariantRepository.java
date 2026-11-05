package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    boolean existsByBarcode(String barcode);

    boolean existsBySku(String sku);

    Optional<ProductVariant> findByBarcode(String barcode);

    @Query("SELECT pv.barcode FROM ProductVariant pv WHERE pv.barcode IN :barcodes")
    Set<String> findExistingBarcodes(@Param("barcodes") Collection<String> barcodes);

    @Query("SELECT pv.sku FROM ProductVariant pv WHERE pv.sku IN :skus")
    Set<String> findExistingSkus(@Param("skus") Collection<String> skus);
}
