package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
