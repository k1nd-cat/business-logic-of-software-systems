package io.blss.lab1.service;

import io.blss.lab1.dto.OrderRequest;
import io.blss.lab1.dto.OrderResponse;
import io.blss.lab1.entity.*;
import io.blss.lab1.exception.InvalidOrderException;
import io.blss.lab1.repository.OrderItemRepository;
import io.blss.lab1.repository.OrderRepository;
import io.blss.lab1.repository.PaymentInfoRepository;
import io.blss.lab1.repository.PersonalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final UserService userService;

    private final PersonalInfoRepository personalInfoRepository;

    private final OrderItemRepository orderItemRepository;

    private final ShoppingCartService shoppingCartService;

    private final PaymentInfoRepository paymentInfoRepository;

    @Transactional
    public OrderResponse makeOrder(OrderRequest orderRequest) {
        final User currentUser = userService.getCurrentUser();
        final var actualPaymentInfo = paymentInfoRepository.findByUserAndIsActual(currentUser, true).orElse(null);

        if(actualPaymentInfo != null) {
            actualPaymentInfo.setActual(false);
            paymentInfoRepository.save(actualPaymentInfo);
        }

        var paymentInfo = paymentInfoRepository.findByUserAndCardNumber(currentUser, orderRequest.getCardNumber())
                .orElse(PaymentInfo.builder()
                        .cardNumber(orderRequest.getCardNumber())
                        .user(currentUser)
                        .build());
        paymentInfo.setActual(true);
        paymentInfo = paymentInfoRepository.save(paymentInfo);

//        Заполняем обновленные данные PersonalInfo
        var personalInfo = personalInfoRepository.findByUser(currentUser)
                .orElse(PersonalInfo.builder().user(currentUser).build());
        personalInfo = orderRequest.updatePersonalInfo(personalInfo);
        personalInfoRepository.save(personalInfo);

//        Заполняем данные заказа
        final var orderBuilder = orderRequest.toOrder();
        final var fullPrice = shoppingCartService.getPrice();
        orderBuilder.setUser(currentUser);
        orderBuilder.setPaymentInfo(paymentInfo);
        orderBuilder.setStatus(Order.OrderStatus.PROCESSING);
        orderBuilder.setOrderAmount(fullPrice);
        orderBuilder.setCreatedAt(new Date());
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

        return OrderResponse.fromOrderAndPersonalInfoAndPaymentInfo(order, personalInfo, paymentInfo);
    }

    public Page<OrderResponse> getOrderHistory(Pageable pageable) {
        final var user = userService.getCurrentUser();
        final var orders = orderRepository.findAllByUserOrderByCreatedAtDesc(user, pageable);
        return orders.map((order) -> OrderResponse.fromOrderAndPersonalInfoAndPaymentInfo(order, user.getPersonalInfo(), order.getPaymentInfo()));
    }
}
