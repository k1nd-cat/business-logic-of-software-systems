package io.blss.lab1.dto;

import io.blss.lab1.entity.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Информация о товаре в корзине покупателя")
@Data
@Builder
public class ProductInCartResponse {
    @Schema(description = "ID элемента корзины", example = "5")
    private Long id;

    @Schema(description = "ID товара", example = "10")
    private Long productId;

    @Schema(description = "Название товара",
            example = "Смартфон Galaxy S23",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "Описание товара",
            example = "Флагманский смартфон с камерой 200 МП")
    private String description;

    @Schema(description = "Цена за единицу товара",
            example = "899.99",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Double price;

    @Schema(description = "Количество товара в корзине",
            example = "2",
            minimum = "1")
    private Integer quantity;

    public static ProductInCartResponse fromCartItem(CartItem cartItem) {
        final var product = cartItem.getProduct();
        return ProductInCartResponse.builder()
                .id(cartItem.getId())
                .productId(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
