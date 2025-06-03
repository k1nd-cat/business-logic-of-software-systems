package io.blss.lab1.controller;

import io.blss.lab1.dto.OrderRequest;
import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.entity.Order;
import io.blss.lab1.repository.OrderRepository;
import io.blss.lab1.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Заказы", description = "Управление заказами")
public class OrderController {
    private final OrderService orderService;

    private final OrderRepository orderRepository;

    @PostMapping
    @Operation(summary = "Создать заказ")
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.makeOrder(orderRequest);
    }

/*
    @PostMapping("cancel/{orderId}")
    @Operation(summary = "Отменить заказ")
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }
*/

    @GetMapping
    @Operation(summary = "История заказов")
    public Page<OrderResponse> getOrderHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrderHistory(PageRequest.of(page, size));
    }

    @PostMapping("/pay/{order_id}")
    @Operation(summary = "История заказов")
    public void payOrder(@PathVariable Long order_id) {
        final var order = orderRepository.findById(order_id).orElseThrow(() -> new RuntimeException());
        order.setStatus(Order.OrderStatus.PROCESSING);
        orderRepository.save(order);
    }
}
