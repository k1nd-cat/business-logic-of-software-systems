package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.PersonalInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Schema(description = "Полная информация о заказе")
@Data
@Builder
public class OrderResponse {
    @Schema(description = "Номер телефона", example = "+79123456789")
    private String number;

    @Schema(description = "Номер карты", example = "4111111111111111")
    private String cardNumber;

    @Schema(description = "Адрес доставки", example = "ул. Ленина, д. 10, кв. 5")
    private String address;

    @Schema(description = "Сумма заказа", example = "1999.98")
    private Double orderAmount;

    @Schema(description = "Тип доставки", example = "COURIER")
    private Order.DeliveryType deliveryType;

    @Schema(description = "Статус заказа", example = "PROCESSING")
    private Order.OrderStatus status;

    @Schema(description = "Время доставки", example = "2023-05-16T14:00:00")
    private Date deliveryTime;

    @Schema(description = "Дата создания заказа", example = "2023-05-15T14:30:00")
    private Date createdAt;

    @Schema(description = "Список товаров в заказе")
    private List<OrderItemResponse> orderItems;

    public static OrderResponse fromOrderAndPersonalInfo(Order order, PersonalInfo personalInfo) {
        final var orderItems = order.getOrderItems().stream().map(OrderItemResponse::fromOrderItem).toList();
        return OrderResponse.builder()
                .number(personalInfo.getNumber())
                .cardNumber(personalInfo.getCardNumber())
                .address(order.getAddress())
                .orderAmount(order.getOrderAmount())
                .deliveryTime(order.getDeliveryTime())
                .createdAt(order.getCreatedAt())
                .orderItems(orderItems)
                .deliveryType(order.getDeliveryType())
                .status(order.getStatus())
                .build();
    }
}
