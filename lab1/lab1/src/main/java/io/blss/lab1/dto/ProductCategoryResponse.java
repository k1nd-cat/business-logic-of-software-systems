package io.blss.lab1.dto;

import io.blss.lab1.entity.ProductCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Информация о категории товаров")
@Data
@Builder
public class ProductCategoryResponse {
    @Schema(description = "ID категории", example = "1")
    private Long id;

    @Schema(description = "Название категории",
            example = "Электроника",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Описание категории",
            example = "Техника и электронные устройства")
    private String description;

    public static ProductCategoryResponse fromProductCategory(ProductCategory productCategory) {
        return ProductCategoryResponse.builder()
                .id(productCategory.getId())
                .title(productCategory.getTitle())
                .description(productCategory.getDescription())
                .build();
    }
}
