package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.User;
import com.fashionshop.fashion_shop.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query(value = "SELECT u FROM User u " +
            "LEFT JOIN FETCH u.store s " +
            "WHERE (:storeId IS NULL OR s.id = :storeId) " +
            "AND (:role IS NULL OR u.role = :role)",
            countQuery = "SELECT COUNT(u) FROM User u " +
            "WHERE (:storeId IS NULL OR u.store.id = :storeId) " +
            "AND (:role IS NULL OR u.role = :role)")
    Page<User> findUsers(
            @Param("storeId") Long storeId,
            @Param("role") RoleType role,
            Pageable pageable
    );
}
