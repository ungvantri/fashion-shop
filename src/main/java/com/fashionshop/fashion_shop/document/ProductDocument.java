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

    @Field(type = FieldType.Long)
    private Long categoryId;

    @Field(type = FieldType.Keyword)
    private String categoryName;

    @Field(type = FieldType.Long)
    private Long brandId;

    @Field(type = FieldType.Keyword)
    private String brandName;

    @Field(type = FieldType.Keyword)
    private Set<String> colors;

    @Field(type = FieldType.Keyword)
    private Set<String> sizes;

    @Field(type = FieldType.Double)
    private BigDecimal minPrice;

    @Field(type = FieldType.Double)
    private BigDecimal maxPrice;

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
