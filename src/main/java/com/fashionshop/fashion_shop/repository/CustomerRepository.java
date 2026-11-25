package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT c FROM Customer c " +
            "JOIN FETCH c.tier t " +
            "WHERE (:search IS NULL OR (" +
            "  LOWER(c.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "  c.phone LIKE CONCAT('%', :search, '%')" +
            ")) " +
            "AND (:tierId IS NULL OR t.id = :tierId)",
            countQuery = "SELECT COUNT(c) FROM Customer c " +
            "WHERE (:search IS NULL OR (" +
            "  LOWER(c.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "  c.phone LIKE CONCAT('%', :search, '%')" +
            ")) " +
            "AND (:tierId IS NULL OR c.tier.id = :tierId)")
    Page<Customer> findCustomers(
            @Param("search") String search,
            @Param("tierId") Long tierId,
            Pageable pageable
    );

    @Query("SELECT c FROM Customer c JOIN FETCH c.tier WHERE c.phone = :phone")
    Optional<Customer> findByPhone(@Param("phone") String phone);

    boolean existsByPhone(String phone);
}
