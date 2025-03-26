package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.OrderItem;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    String number;
    String card_number;
    String address;
    //сумма заказа
    private Double orderAmount;
    Order.DeliveryType deliveryType;
    LocalDateTime delivery_time;
    List<OrderItem> orderItems;
}
