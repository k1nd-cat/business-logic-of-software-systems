package io.blss.lab1.service;

import io.blss.lab1.dto.OrderRequest;
import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.entity.*;
import io.blss.lab1.exception.InvalidOrderException;
import io.blss.lab1.repository.OrderItemRepository;
import io.blss.lab1.repository.OrderRepository;
import io.blss.lab1.repository.PersonalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final UserService userService;

    private final PersonalInfoRepository personalInfoRepository;

    private final OrderItemRepository orderItemRepository;

    private final ShoppingCartService shoppingCartService;

    @Transactional
    public OrderResponse makeOrder(OrderRequest orderRequest) {
        final User currentUser = userService.getCurrentUser();

//        Заполняем обновленные данные PersonalInfo
        var personalInfo = personalInfoRepository.findByUser(currentUser)
                .orElse(PersonalInfo.builder().user(currentUser).build());
        personalInfo = orderRequest.updatePersonalInfo(personalInfo);
        personalInfoRepository.save(personalInfo);

//        Заполняем данные заказа
        final var orderBuilder = orderRequest.toOrder();
        final var fullPrice = shoppingCartService.getPrice();
        orderBuilder.setUser(currentUser);
        orderBuilder.setStatus(Order.OrderStatus.PROCESSING);
        orderBuilder.setOrderAmount(fullPrice);
        orderBuilder.setCreatedAt(LocalDateTime.now());
        final var order = orderRepository.save(orderBuilder);

//        Переносим продукты из корзины в данные заказа и чистим данные корзины
        final var shoppingCart = currentUser.getShoppingCart();
        if (shoppingCart.getItems() == null || shoppingCart.getItems().isEmpty())
            throw new InvalidOrderException("Невозможно создать пустой заказ");
        final var orderItems = new ArrayList<OrderItem>();
        List<CartItem> cartItems = new ArrayList<>(shoppingCart.getItems());
        for (CartItem cartItem : cartItems) {
            var orderItem = OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .build();
            orderItems.add(orderItemRepository.save(orderItem));

            shoppingCart.getItems().remove(cartItem);
            cartItem.setCart(null);
        }

        order.setOrderItems(orderItems);

        return OrderResponse.fromOrderAndPersonalInfo(order, personalInfo);
    }

    public Page<OrderResponse> getOrderHistory(Pageable pageable) {
        final var user = userService.getCurrentUser();
        final var orders = orderRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);
        return orders.map((order) -> OrderResponse.fromOrderAndPersonalInfo(order, user.getPersonalInfo()));
    }
}
