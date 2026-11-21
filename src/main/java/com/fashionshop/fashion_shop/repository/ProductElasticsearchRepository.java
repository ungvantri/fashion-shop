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

    @Query("""
    {
      "bool": {
        "should": [
          {
            "multi_match": {
              "query": "?0",
              "fields": ["name^3", "code^2", "description"],
              "fuzziness": "AUTO"
            }
          },
          {
            "nested": {
              "path": "variants",
              "query": {
                "multi_match": {
                  "query": "?0",
                  "fields": ["variants.sku^3", "variants.barcode^3"]
                }
              }
            }
          }
        ],
        "minimum_should_match": 1
      }
    }
    """)
    Page<ProductDocument> searchForInventory(String keyword, Pageable pageable);
}
