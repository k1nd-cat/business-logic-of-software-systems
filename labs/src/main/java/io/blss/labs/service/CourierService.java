package io.blss.labs.service;

import io.blss.labs.dto.OrderForCourierResponse;
import io.blss.labs.entity.Order;
import io.blss.labs.entity.User;
import io.blss.labs.exception.OrderNotAvailableException;
import io.blss.labs.exception.OrderNotFoundException;
import io.blss.labs.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final OrderRepository orderRepository;

    private final UserService userService;

    public Page<OrderForCourierResponse> getProcessingOrders(Pageable pageable) {
        final var orders = orderRepository.findAllByStatusOrderByCreatedAtAsc(Order.OrderStatus.PROCESSING, pageable);
        return orders.map(OrderForCourierResponse::fromOrder);
    }

    public OrderForCourierResponse selectOrderForDelivery(Long orderId) {
        final var user = userService.getCurrentUser();
        return selectOrderForDeliveryNew(orderId, user);
    }

    public OrderForCourierResponse selectOrderForDelivery(Long orderId, User user) {
        return selectOrderForDeliveryNew(orderId, user);
    }

    public OrderForCourierResponse selectOrderForDeliveryNew(Long orderId, User user) {
        final var order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Такого заказа не существует")
        );

        if (order.getStatus() != Order.OrderStatus.PROCESSING)
            throw new OrderNotAvailableException("Данный заказ уже обработан");

        order.setStatus(Order.OrderStatus.SHIPPED);
        order.setCourier(user);
        orderRepository.save(order);

        return OrderForCourierResponse.fromOrder(order);
    }

    public Page<OrderForCourierResponse> getSelectedOrders(Pageable pageable) {
        var user  = userService.getCurrentUser();
        return getSelectedOrdersNew(pageable, user);
    }
    
    public Page<OrderForCourierResponse> getSelectedOrders(Pageable pageable, User user) {
        return getSelectedOrdersNew(pageable, user);
    }

    public Page<OrderForCourierResponse> getSelectedOrdersNew(Pageable pageable, User user) {
        final var orders = orderRepository.findAllByCourierAndStatus(user, Order.OrderStatus.SHIPPED, pageable);
        return orders.map(OrderForCourierResponse::fromOrder);
    }

    public void deliverOrder(Long orderId) {
        var user = userService.getCurrentUser();
        deliverOrderNew(orderId, user);
    }

    public void deliverOrderNew(Long orderId, User user) {
        final var order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Заказ не найден")
        );

        if (order.getCourier() != user || order.getStatus() != Order.OrderStatus.SHIPPED)
            throw new OrderNotAvailableException("Текущий курьер не может доставить заказ");

        order.setStatus(Order.OrderStatus.DELIVERED);
        order.setDeliveredAt(new Date());
        orderRepository.save(order);
    }
}
