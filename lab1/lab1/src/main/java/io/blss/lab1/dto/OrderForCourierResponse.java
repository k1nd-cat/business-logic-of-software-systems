package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Информация о заказе для курьера")
@Data
@Builder
public class OrderForCourierResponse {

    @Schema(description = "ID заказа", example = "1")
    private Long id;

    @Schema(description = "Адрес доставки", example = "ул. Ленина, д. 10, кв. 5")
    private String address;

    @Schema(description = "Статус заказа", example = "PROCESSING")
    private Order.OrderStatus status;

    @Schema(description = "Тип доставки", example = "COURIER")
    private Order.DeliveryType type;

    @Schema(description = "Дата создания заказа", example = "2023-05-15T14:30:00")
    private LocalDateTime createdAt;

    public static OrderForCourierResponse fromOrder(Order order) {
        return OrderForCourierResponse.builder()
                .address(order.getAddress())
                .status(order.getStatus())
                .type(order.getDeliveryType())
                .createdAt(order.getCreatedAt())
                .id(order.getId())
                .build();
    }
}
