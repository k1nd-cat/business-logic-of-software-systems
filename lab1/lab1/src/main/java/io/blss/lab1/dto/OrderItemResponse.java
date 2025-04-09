package io.blss.lab1.dto;

import io.blss.lab1.entity.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Информация о товаре в заказе")
@Data
@Builder
public class OrderItemResponse {
    @Schema(description = "ID элемента заказа", example = "1")
    private Long id;

    @Schema(description = "ID товара", example = "5")
    private Long productId;

    @Schema(description = "Название товара", example = "Смартфон")
    private String title;

    @Schema(description = "Описание товара", example = "Новый флагманский смартфон")
    private String description;

    @Schema(description = "Цена товара", example = "999.99")
    private Double price;

    @Schema(description = "Количество товара", example = "1")
    private Integer quantity;

    public static OrderItemResponse fromOrderItem(OrderItem orderItem) {
        final var product = orderItem.getProduct();
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .productId(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
