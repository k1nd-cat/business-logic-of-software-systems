package io.blss.lab1.dto;

import io.blss.lab1.entity.OrderItem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String title;
    private String description;
    private Double price;
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
