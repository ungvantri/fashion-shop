package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.CustomerTier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CustomerTierRepository extends JpaRepository<CustomerTier, Long> {
    boolean existsByTierName(String tierName);

    // Lấy hạng thành viên mặc định thấp nhất (minSpent = 0)
    Optional<CustomerTier> findFirstByOrderByMinSpentAsc();

    // Tìm hạng thành viên phù hợp nhất theo tổng chi tiêu
    @Query("SELECT ct FROM CustomerTier ct WHERE ct.minSpent <= :spent ORDER BY ct.minSpent DESC LIMIT 1")
    Optional<CustomerTier> findTierBySpent(BigDecimal spent);
}
