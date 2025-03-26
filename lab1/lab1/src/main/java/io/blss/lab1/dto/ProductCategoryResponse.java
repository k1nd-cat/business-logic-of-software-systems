package io.blss.lab1.dto;

import io.blss.lab1.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryResponse {
    private Long id;
    private String title;
    private String description;

    public static ProductCategoryResponse fromProductCategory(ProductCategory productCategory) {
        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .title(productCategory.getTitle())
                .description(productCategory.getDescription())
                .build();
    }
}
