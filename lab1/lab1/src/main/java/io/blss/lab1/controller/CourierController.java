package io.blss.lab1.controller;

import io.blss.lab1.dto.OrderForCourierResponse;
import io.blss.lab1.service.CourierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/couriers")
@Tag(name = "Курьеры", description = "Управление заказами курьера")
public class CourierController {
    private final CourierService courierService;

    @GetMapping("/orders/available")
    @Operation(summary = "Доступные заказы")
    public Page<OrderForCourierResponse> getAvailableOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return courierService.getProcessingOrders(PageRequest.of(page, size));
    }

    @PatchMapping("/orders/{orderId}/accept")
    @Operation(summary = "Принять заказ")
    public OrderForCourierResponse acceptOrder(@PathVariable Long orderId) {
        return courierService.selectOrderForDelivery(orderId);
    }

    @GetMapping("/orders/accepted")
    @Operation(summary = "Принятые заказы")
    public Page<OrderForCourierResponse> getAcceptedOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return courierService.getSelectedOrders(PageRequest.of(page, size));
    }

    @PatchMapping("/orders/{orderId}/complete")
    @Operation(summary = "Завершить заказ")
    public void completeOrder(@PathVariable Long orderId) {
        courierService.deliverOrder(orderId);
    }
}
