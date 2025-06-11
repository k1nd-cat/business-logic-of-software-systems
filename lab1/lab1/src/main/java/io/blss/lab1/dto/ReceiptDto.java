package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDto {
    private Long userId;
    private Long orderId;
    private Double price;
    private Double discount;
    private List<ReceiptProduct> receiptProductList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptProduct {
        private String name;
        private Integer quantity;
        private Double price;

        public static ReceiptProduct fromOrderItem(OrderItem orderItem) {
            return  ReceiptProduct.builder()
                    .name(orderItem.getProduct().getTitle())
                    .quantity(orderItem.getQuantity())
                    .price(orderItem.getProduct().getPrice())
                    .build();
        }
    }

    public static ReceiptDto fromOrder(Order order) {
        final var receiptProductList = order.getOrderItems().stream().map(ReceiptProduct::fromOrderItem).toList();
        return ReceiptDto.builder()
                .userId(order.getUser().getId())
                .orderId(order.getId())
                .price(order.getOrderAmount())
                .discount(order.getPromoCode() != null ? order.getPromoCode().getPercentage() : null)
                .receiptProductList(receiptProductList)
                .build();
    }
}
