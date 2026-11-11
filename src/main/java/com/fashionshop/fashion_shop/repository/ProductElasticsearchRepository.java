package com.fashionshop.fashion_shop.repository;

import com.fashionshop.fashion_shop.document.ProductDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

    // =========================================================================
    // 1. Tìm kiếm Full-Text theo từ khóa (Name, Code, Description) kèm chống gõ sai
    // ?0: từ khóa tìm kiếm (keyword)
    // =========================================================================
    @Query("""
    {
      "bool": {
        "must": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["name^3", "code^2", "description"],
              "fuzziness": "AUTO"
            }
          }
        ],
        "filter": [
          { "term": { "isActive": true } }
        ]
      }
    }
    """)
    Page<ProductDocument> searchByKeyword(String keyword, Pageable pageable);

    // =========================================================================
    // 2. Tìm kiếm theo từ khóa VÀ Lọc theo Danh mục
    // ?0: từ khóa, ?1: categoryId
    // =========================================================================
    @Query("""
    {
      "bool": {
        "must": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["name^3", "code^2", "description"],
              "fuzziness": "AUTO"
            }
          }
        ],
        "filter": [
          { "term": { "categoryId": ?1 } },
          { "term": { "isActive": true } }
        ]
      }
    }
    """)
    Page<ProductDocument> searchByKeywordAndCategory(String keyword, Long categoryId, Pageable pageable);

    // =========================================================================
    // 3. Chỉ chọn Danh mục (không gõ từ khóa)
    // ?0: categoryId
    // =========================================================================
    @Query("""
    {
      "bool": {
        "filter": [
          { "term": { "categoryId": ?0 } },
          { "term": { "isActive": true } }
        ]
      }
    }
    """)
    Page<ProductDocument> findByCategoryId(Long categoryId, Pageable pageable);

    // =========================================================================
    // 4. Lọc sản phẩm theo Màu sắc và Kích cỡ
    // ?0: màu sắc (ví dụ: "Đen"), ?1: kích cỡ (ví dụ: "M")
    // =========================================================================
    @Query("""
    {
      "bool": {
        "filter": [
          { "term": { "colors": "?0" } },
          { "term": { "sizes": "?1" } },
          { "term": { "isActive": true } }
        ]
      }
    }
    """)
    Page<ProductDocument> findByColorAndSize(String color, String size, Pageable pageable);

    // =========================================================================
    // 5. Lọc theo Khoảng giá bán (Price Range)
    // ?0: minPrice, ?1: maxPrice
    // =========================================================================
    @Query("""
    {
      "bool": {
        "filter": [
          {
            "range": {
              "minPrice": { "gte": ?0, "lte": ?1 }
            }
          },
          { "term": { "isActive": true } }
        ]
      }
    }
    """)
    Page<ProductDocument> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
