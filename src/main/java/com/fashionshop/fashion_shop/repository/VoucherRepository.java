package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

    Optional<Voucher> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT v FROM Voucher v WHERE (:search IS NULL OR LOWER(v.code) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Voucher> findVouchers(@Param("search") String search, Pageable pageable);
}
