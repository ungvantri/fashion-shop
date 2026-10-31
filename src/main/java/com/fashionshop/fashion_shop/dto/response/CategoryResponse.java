package com.fashionshop.fashion_shop.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String slug;
    private Long parentId;
    private String parentName;
    private List<CategoryResponse> children;
}
