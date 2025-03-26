package io.blss.lab1.dto;

import io.blss.lab1.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInCartResponse {
    private Long id;
    private Long productId;
    private String title;
    private String description;
    private Double price;
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
