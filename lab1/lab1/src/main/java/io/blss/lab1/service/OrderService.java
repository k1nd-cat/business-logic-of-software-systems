package io.blss.lab1.service;

import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.entity.User;
import io.blss.lab1.repository.OrderRepository;
import io.blss.lab1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;

    public void makeOrder(OrderResponse orderResponse) {
        User currentUser = userService.getCurrentUser();

    }
}
