package io.blss.lab1.controller;

import io.blss.lab1.dto.OrderForCourierResponse;
import io.blss.lab1.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courier")
public class CourierController {

    private final CourierService courierService;

    @GetMapping("/orders/available")
    public List<OrderForCourierResponse> getAvailableOrders() {
        return courierService.getProcessingOrders();
    }

    @PostMapping("/orders/{orderId}/select")
    public OrderForCourierResponse selectOrder(@PathVariable Long orderId) {
        return courierService.selectOrderForDelivery(orderId);
    }

    @GetMapping("/orders/selected")
    public List<OrderForCourierResponse> getSelectedOrders() {
        return courierService.getSelectedOrders();
    }

    @PostMapping("/orders/{orderId}/deliver")
    public void deliverOrder(@PathVariable Long orderId) {
        courierService.deliverOrder(orderId);
    }
}