package io.blss.lab1.controller;

import io.blss.lab1.dto.OrderRequest;
import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/make")
    public OrderResponse makeOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.makeOrder(orderRequest);
    }

    @GetMapping("/history")
    public List<OrderResponse> getOrderHistory() {
        return orderService.getOrderHistory();
    }
}
