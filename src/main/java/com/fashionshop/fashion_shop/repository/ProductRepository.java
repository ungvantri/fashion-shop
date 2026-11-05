package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE" +
           "((:search is NULL) OR (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))))" +
           "AND ((:categoryId is NULL) OR (p.category.id = :categoryId))" +
           "AND ((:season is NULL) OR (LOWER(p.season) LIKE LOWER(CONCAT('%', :season, '%'))))" +
           "AND ((:isActive is NULL) OR (p.isActive = :isActive))"
    )
    Page<Product> findByFilters(@Param("search") String search, @Param("categoryId") Long categoryId, @Param("season") String season, @Param("isActive") Boolean isActive, Pageable pageable);
}
