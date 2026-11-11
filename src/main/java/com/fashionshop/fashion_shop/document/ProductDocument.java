package com.fashionshop.fashion_shop.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Document(indexName = "products")
@Setting(settingPath = "elasticsearch/product-settings.json")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {

    @Id
    private Long id;

    // FieldType.Text với vietnamese_analyzer: Tự động chuyển chữ hoa thành thường + bỏ dấu tiếng Việt (asciifolding)
    // Sub-field .keyword: Dùng để lọc chính xác hoặc sắp xếp (Sort)
    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer", searchAnalyzer = "vietnamese_analyzer"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String code;

    @MultiField(
            mainField = @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer", searchAnalyzer = "vietnamese_analyzer"),
            otherFields = {
                    @InnerField(suffix = "keyword", type = FieldType.Keyword)
            }
    )
    private String name;

    @Field(type = FieldType.Keyword)
    private String season;

    @Field(type = FieldType.Boolean)
    private Boolean isActive;

    @Field(type = FieldType.Text, analyzer = "vietnamese_analyzer", searchAnalyzer = "vietnamese_analyzer")
    private String description;

    // Thông tin Category (lưu sẵn tên để không cần join)
    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    // Thông tin Brand
    @Field(type = FieldType.Long)
    private Long brandId;

    @Field(type = FieldType.Keyword)
    private String brandName;

    // Gom các thuộc tính của Variants lên Product để lọc siêu tốc
    @Field(type = FieldType.Keyword)
    private Set<String> colors; // Ví dụ: ["Đỏ", "Xanh", "Trắng"]

    @Field(type = FieldType.Keyword)
    private Set<String> sizes;  // Ví dụ: ["S", "M", "L", "XL"]

    @Field(type = FieldType.Double)
    private BigDecimal minPrice; // Giá thấp nhất trong các variant (để lọc khoảng giá)

    @Field(type = FieldType.Double)
    private BigDecimal maxPrice; // Giá cao nhất trong các variant

    // Chi tiết từng biến thể
    @Field(type = FieldType.Nested)
    private List<VariantDocument> variants;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariantDocument {
        private Long id;
        private String sku;
        private String barcode;
        private String size;
        private String color;
        private BigDecimal costPrice;
        private BigDecimal retailPrice;
        private String imageUrl;
    }
}
