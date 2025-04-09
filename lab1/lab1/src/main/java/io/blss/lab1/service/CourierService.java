package io.blss.lab1.service;

import io.blss.lab1.dto.OrderForCourierResponse;
import io.blss.lab1.entity.Order;
import io.blss.lab1.exception.OrderNotAvailableException;
import io.blss.lab1.exception.OrderNotFoundException;
import io.blss.lab1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final OrderRepository orderRepository;

    private final UserService userService;

    public List<OrderForCourierResponse> getProcessingOrders() {
        final var orders = orderRepository.findAllByStatusOrderByCreatedAtAsc(Order.OrderStatus.PROCESSING);
        return orders.stream().map(OrderForCourierResponse::fromOrder).toList();
    }

    public OrderForCourierResponse selectOrderForDelivery(Long orderId) {
        final var order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Такого заказа не существует")
        );

        if (order.getStatus() != Order.OrderStatus.PROCESSING)
            throw new OrderNotAvailableException("Данный заказ уже обработан");

        final var user = userService.getCurrentUser();
        order.setStatus(Order.OrderStatus.SHIPPED);
        order.setCourier(user);
        orderRepository.save(order);

        return OrderForCourierResponse.fromOrder(order);
    }

    public List<OrderForCourierResponse> getSelectedOrders() {
        final var user = userService.getCurrentUser();
        final var orders = orderRepository.findAllByCourierAndStatus(user, Order.OrderStatus.SHIPPED);
        return orders.stream().map(OrderForCourierResponse::fromOrder).toList();
    }

    public void deliverOrder(Long orderId) {
        final var user = userService.getCurrentUser();
        final var order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Заказ не найден")
        );

        if (order.getCourier() != user || order.getStatus() != Order.OrderStatus.SHIPPED)
            throw new OrderNotAvailableException("Текущий курьер не может доставить заказ");

        order.setStatus(Order.OrderStatus.DELIVERED);
        order.setDeliveredAt(LocalDateTime.now());
        orderRepository.save(order);
    }
}
