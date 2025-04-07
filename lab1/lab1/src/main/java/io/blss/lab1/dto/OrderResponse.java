package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.OrderItem;
import io.blss.lab1.entity.PersonalInfo;
import io.blss.lab1.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    String number;
    String cardNumber;
    String address;
    //сумма заказа
    private Double orderAmount;
    Order.DeliveryType deliveryType;
    LocalDateTime deliveryTime;
    List<OrderItem> orderItems;

    public Order toOrder(User user) {
        return Order.builder()
                .user(user)
                .address(address)
                .orderAmount(orderAmount)
                .deliveryType(deliveryType)
                .orderItems(orderItems)
                .delivery_time(deliveryTime)
                .build();
    }

    public PersonalInfo toPersonalInfo(User user) {
        return PersonalInfo.builder()
                .user(user)
                .cardNumber(cardNumber)
                .number(number)
                .build();
    }
}
