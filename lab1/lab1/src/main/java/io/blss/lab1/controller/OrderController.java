package io.blss.lab1.controller;

import io.blss.lab1.dto.OrderRequest;
import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Заказы", description = "Управление заказами")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Создать заказ")
    public String createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.makeOrder(orderRequest);
    }

    @GetMapping
    @Operation(summary = "История заказов")
    public Page<OrderResponse> getOrderHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return orderService.getOrderHistory(PageRequest.of(page, size));
    }

    @GetMapping("/receipt/{orderId}")
    @Operation(summary = "Получить чек по id заказа")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable Long orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lab_shop_receipt");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");

        final var fileStream = orderService.getReceipt(orderId);

        InputStreamResource resource = new InputStreamResource(fileStream);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

}
