package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.OrderItem;
import io.blss.lab1.entity.PersonalInfo;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private String number;
    private String cardNumber;
    private String address;
    private Double orderAmount;
    private Order.DeliveryType deliveryType;
    private LocalDateTime deliveryTime;
    private List<OrderItemResponse> orderItems;

    public static OrderResponse fromOrderAndPersonalInfo(Order order, PersonalInfo personalInfo) {
        final var orderItems = order.getOrderItems().stream().map(OrderItemResponse::fromOrderItem).toList();
        return OrderResponse.builder()
                .number(personalInfo.getNumber())
                .cardNumber(personalInfo.getCardNumber())
                .address(order.getAddress())
                .orderAmount(order.getOrderAmount())
                .deliveryTime(order.getDeliveryTime())
                .orderItems(orderItems)
                .build();
    }
}
