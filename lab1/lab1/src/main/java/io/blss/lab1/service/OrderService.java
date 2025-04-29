package io.blss.lab1.service;

import io.blss.lab1.dto.OrderRequest;
import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.entity.*;
import io.blss.lab1.exception.InvalidOrderException;
import io.blss.lab1.repository.OrderItemRepository;
import io.blss.lab1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final UserService userService;

    private final OrderItemRepository orderItemRepository;

    private final ShoppingCartService shoppingCartService;

    private final PersonService personService;

    private final PlatformTransactionManager transactionManager;

    private final DataSource dataSource;

    @Transactional
    public OrderResponse makeOrder(OrderRequest orderRequest) {

        System.out.println(">>> Transaction Manager: " + transactionManager.getClass().getName());
        System.out.println(">>> DataSource class: " + dataSource.getClass().getName()); // Выведи класс DataSource
        final var user = userService.getCurrentUser();
        final var paymentInfo = personService.changeActualPaymentInfo(user, orderRequest.getCardNumber());
        final var personalInfo = personService.updatePersonalInfo(user, orderRequest);
        final var order = orderRepository.save(buildOrder(user, paymentInfo, orderRequest));
        moveProducts2Order(user, order);

        return OrderResponse.fromOrderAndPersonalInfoAndPaymentInfo(order, personalInfo, paymentInfo);
    }

    public Page<OrderResponse> getOrderHistory(Pageable pageable) {
        final var user = userService.getCurrentUser();
        final var orders = orderRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);
        return orders.map(
                (order) ->
                        OrderResponse.fromOrderAndPersonalInfoAndPaymentInfo(order, user.getPersonalInfo(), order.getPaymentInfo())
        );
    }

    private Order buildOrder(User user, PaymentInfo paymentInfo, OrderRequest orderRequest) {
        final var orderBuilder = orderRequest.toOrder();
        final var fullPrice = shoppingCartService.getPrice();
        orderBuilder.setUser(user);
        orderBuilder.setPaymentInfo(paymentInfo);
        orderBuilder.setStatus(Order.OrderStatus.PROCESSING);
        orderBuilder.setOrderAmount(fullPrice);
        orderBuilder.setCreatedAt(new Date());

        return orderBuilder;
    }

    private void moveProducts2Order(User user, Order order) {
        final var shoppingCart = user.getShoppingCart();
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
    }
}
