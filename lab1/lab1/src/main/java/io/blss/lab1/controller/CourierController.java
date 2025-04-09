package io.blss.lab1.controller;

import io.blss.lab1.dto.OrderForCourierResponse;
import io.blss.lab1.service.CourierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courier")
public class CourierController {

    private final CourierService courierService;

    @GetMapping("/orders/available")
    public Page<OrderForCourierResponse> getAvailableOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return courierService.getProcessingOrders(pageable);
    }

    @PostMapping("/orders/{orderId}/select")
    public OrderForCourierResponse selectOrder(@PathVariable Long orderId) {
        return courierService.selectOrderForDelivery(orderId);
    }

    @GetMapping("/orders/selected")
    public Page<OrderForCourierResponse> getSelectedOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return courierService.getSelectedOrders(pageable);
    }

    @PostMapping("/orders/{orderId}/deliver")
    public void deliverOrder(@PathVariable Long orderId) {
        courierService.deliverOrder(orderId);
    }
}