package io.blss.lab1.dto;

import io.blss.lab1.entity.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Информация о товаре в корзине")
@Data
@Builder
public class CartItemResponse {
    @Schema(description = "ID элемента корзины", example = "1")
    private Long id;

    @Schema(description = "ID товара", example = "5")
    private Long productId;

    @Schema(description = "Название товара", example = "Смартфон")
    private String title;

    @Schema(description = "Описание товара", example = "Новый флагманский смартфон")
    private String description;

    @Schema(description = "Цена товара", example = "999.99")
    private Double price;

    @Schema(description = "Количество товара", example = "2")
    private Integer quantity;

    public static CartItemResponse fromCartItem(CartItem cartItem) {
        final var product = cartItem.getProduct();
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .productId(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
