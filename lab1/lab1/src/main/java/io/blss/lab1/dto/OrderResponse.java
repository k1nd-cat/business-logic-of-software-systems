package io.blss.lab1.dto;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.OrderItem;
import io.blss.lab1.entity.PersonalInfo;
import io.blss.lab1.entity.User;
import jakarta.persistence.*;

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

    public static Order fromOrderResponseToOrder(OrderResponse orderResponse, User user) {
        return Order.builder()
                .user(user)
                .address(orderResponse.address)
                .orderAmount(orderResponse.orderAmount)
                .deliveryType(orderResponse.deliveryType)
                .orderItems(orderResponse.orderItems)
                .delivery_time(orderResponse.deliveryTime)
                .build();
    }

    public static PersonalInfo fromOrderResponseToPersonalInfo(OrderResponse orderResponse, User user) {
        return PersonalInfo.builder()
                .user(user)
                .cardNumber(orderResponse.cardNumber)
                .number(orderResponse.number)
                .build();
    }
}
