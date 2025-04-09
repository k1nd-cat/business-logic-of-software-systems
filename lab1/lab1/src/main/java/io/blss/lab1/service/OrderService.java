package io.blss.lab1.service;

import io.blss.lab1.dto.OrderRequest;
import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.entity.*;
import io.blss.lab1.repository.CartItemRepository;
import io.blss.lab1.repository.OrderItemRepository;
import io.blss.lab1.repository.OrderRepository;
import io.blss.lab1.repository.PersonalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final UserService userService;

    private final PersonalInfoRepository personalInfoRepository;

    private final OrderItemRepository orderItemRepository;

    private final PromoCodeService promoCodeService;

    private final ShoppingCartService shoppingCartService;

    private final CartItemRepository cartItemRepository;

    //    TODO: Добавить в Order поля: createdAt, deliveredAt, canceledAt
    public OrderResponse makeOrder(OrderRequest orderRequest) {
        final User currentUser = userService.getCurrentUser();

//        Заполняем обновленные данные PersonalInfo
        var personalInfo = personalInfoRepository.findByUser(currentUser)
                .orElse(PersonalInfo.builder().user(currentUser).build());
        personalInfo = orderRequest.tupdatePersonalInfo(personalInfo);
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
        final var orderItems = shoppingCart.getItems().stream().map(
                (item) -> {
                    var orderItem = OrderItem.builder()
                            .order(order)
                            .product(item.getProduct())
                            .quantity(item.getQuantity())
                            .build();
                    orderItem = orderItemRepository.save(orderItem);
                    cartItemRepository.delete(item);
                    return orderItem;
                }
        ).toList();
        order.setOrderItems(orderItems);

        return OrderResponse.fromOrderAndPersonalInfo(order, personalInfo);
    }

    public List<OrderResponse> getOrderHistory() {
        final var user = userService.getCurrentUser();
        final var orders = orderRepository.findAllByUserOrderByCreatedAtDesc(user);
        return orders.stream()
                .map((order) -> OrderResponse.fromOrderAndPersonalInfo(order, user.getPersonalInfo()))
                .toList();
    }

//    TODO: Изменить SecurityConfig для Order, разбить по ролям
//    TODO: Получить список заказа (только для сотрудников)
//    TODO: Изменить статус заказа (только для сотрудников)
}
