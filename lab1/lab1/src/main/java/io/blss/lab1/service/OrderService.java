package io.blss.lab1.service;

import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.entity.*;
import io.blss.lab1.repository.OrderItemRepository;
import io.blss.lab1.repository.OrderRepository;
import io.blss.lab1.repository.PersonalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final PersonalInfoRepository personalInfoRepository;

    private final OrderItemRepository orderItemRepository;

    private final PromoCodeService promoCodeService;

    private final ShoppingCartService shoppingCartService;

    //    TODO: Дописать метод: при оформлении заказа, удалять продукты из корзины
//    TODO: Добавить в Order поля: createdAt, deliveredAt, canceledAt
    public void makeOrder(OrderResponse orderResponse) {
        User currentUser = userService.getCurrentUser();
        Order order = orderResponse.toOrder(currentUser);
        PersonalInfo personalInfo = orderResponse.toPersonalInfo(currentUser);

        orderRepository.save(order);
        personalInfoRepository.save(personalInfo);
    }

    private Order generateOrderFromCurrentUserShoppingCart() {
        final var user = userService.getCurrentUser();
        final var shoppingCart = user.getShoppingCart();
        final var orderBuilder = Order.builder()
                .user(user)
                .status(Order.OrderStatus.PROCESSING)
                .build();

        final var fullPrice = shoppingCartService.getPrice();
        orderBuilder.setOrderAmount(fullPrice);
        final var order = orderRepository.save(orderBuilder);
        shoppingCart.getItems().forEach(
                (item) -> {
                    final var orderItemBuilder = OrderItem.builder()
                            .order(order)
                            .product(item.getProduct())
                            .quantity(item.getQuantity())
                            .build();
                    orderItemRepository.save(orderItemBuilder);
                }
        );

        return order;
    }

//    TODO: Изменить SecurityConfig для Order, разбить по ролям
//    TODO: Отменить заказ (только для покупателя)
//    TODO: Получить список заказа (только для сотрудников)
//    TODO: Изменить статус заказа (только для сотрудников)
}
