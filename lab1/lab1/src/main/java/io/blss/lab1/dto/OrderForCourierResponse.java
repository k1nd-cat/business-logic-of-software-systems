package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderForCourierResponse {
    private String address;
    private Order.OrderStatus status;
    private Order.DeliveryType type;
    private LocalDateTime createdAt;

    public static OrderForCourierResponse fromOrder(Order order) {
        return OrderForCourierResponse.builder()
                .address(order.getAddress())
                .status(order.getStatus())
                .type(order.getDeliveryType())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
