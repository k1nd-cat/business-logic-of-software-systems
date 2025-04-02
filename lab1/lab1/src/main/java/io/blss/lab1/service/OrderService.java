package io.blss.lab1.service;

import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.PersonalInfo;
import io.blss.lab1.entity.User;
import io.blss.lab1.repository.OrderRepository;
import io.blss.lab1.repository.PersonalInfoRepository;
import io.blss.lab1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.blss.lab1.dto.OrderResponse.*;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final PersonalInfoRepository personalInfoRepository;

    public void makeOrder(OrderResponse orderResponse) {
        User currentUser = userService.getCurrentUser();
        Order order = OrderResponse.fromOrderResponseToOrder(orderResponse, currentUser);
        PersonalInfo personalInfo = fromOrderResponseToPersonalInfo(orderResponse, currentUser);

        orderRepository.save(order);
        personalInfoRepository.save(personalInfo);
    }
}
