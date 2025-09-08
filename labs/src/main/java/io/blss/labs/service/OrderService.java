package io.blss.labs.service;

import io.blss.labs.dto.OrderRequest;
import io.blss.labs.dto.OrderResponse;
import io.blss.labs.dto.ReceiptDto;
import io.blss.labs.entity.*;
import io.blss.labs.exception.InvalidOrderException;
import io.blss.labs.exception.OrderNotAvailableException;
import io.blss.labs.exception.OrderNotFoundException;
import io.blss.labs.repository.OrderItemRepository;
import io.blss.labs.repository.OrderRepository;
import io.blss.labs.repository.PaymentInfoRepository;
import io.blss.labs.repository.ProductRepository;
import io.blss.labs.robokassa.Payment;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final UserService userService;

    private final OrderItemRepository orderItemRepository;

    private final ShoppingCartService shoppingCartService;

    private final PersonService personService;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final PaymentInfoRepository paymentInfoRepository;

    private final ProductRepository productRepository;

    private final JmsService jmsService;

    private final MinioService minioService;

    private final PaymentService paymentService;

    @Transactional
    public String makeOrder(OrderRequest orderRequest) {
        var user = userService.getCurrentUser();
        return makeOrder(orderRequest, user);
    }

    @Transactional
    public String makeOrder(OrderRequest orderRequest, User user) {

        var actualPaymentInfo = paymentInfoRepository.findByUserAndIsActual(user, true).orElse(null);
        log.info("Актуальный номер карты до выполнения изменений: {}", actualPaymentInfo != null ? actualPaymentInfo.getCardNumber() : "Не существует");
        final var paymentInfo = personService.changeActualPaymentInfo(user, orderRequest.getCardNumber());
        final var personalInfo = personService.updatePersonalInfo(user, orderRequest);
        final var order = orderRepository.save(buildOrder(user, paymentInfo, orderRequest));
        try {
            moveProducts2Order(user, order);
        } catch (InvalidOrderException e) {
            actualPaymentInfo = paymentInfoRepository.findByUserAndIsActual(user, true).orElse(null);
            log.info("Актуальный номер карты перед исключением: {}", actualPaymentInfo != null ? actualPaymentInfo.getCardNumber() : "Не существует");
            throw e;
        }

        final var payment = Payment.builder()
                .invoiceRef(order.getId().toString())
                .amount(1.0)
                .comment("Оплата товаров")
                .commentData("Комментарий к заказу")
                .build();

        return paymentService.createPaymentUrl(payment);
    }

    public void cancelOrder(Long orderId) {
        final var order = orderRepository.findByIdWithItems(orderId).orElseThrow(
                () -> new OrderNotFoundException("Заказа не существует")
        );
        final var orderItems = order.getOrderItems();
        for (var orderItem : orderItems) {
            final var product = orderItem.getProduct();
            product.setQuantity(product.getQuantity() + orderItem.getQuantity());
            productRepository.save(product);
        }
        order.setCanceledAt(new Date());
        if (order.getStatus() != Order.OrderStatus.CANCELLED_TIMEOUT)
            order.setStatus(Order.OrderStatus.CANCELLED);

        orderRepository.save(order);
    }

    public Page<OrderResponse> getOrderHistory(Pageable pageable) {
        var user =  userService.getCurrentUser();
        return getOrderHistory(pageable, user);
    }

    public Page<OrderResponse> getOrderHistory(Pageable pageable, User user) {
        final var orders = orderRepository.findAllByUserOrderByCreatedAtDescWithItems(user, pageable);
        return orders.map(
                (order) ->
                        OrderResponse.fromOrderAndPersonalInfoAndPaymentInfo(order, user.getPersonalInfo(), order.getPaymentInfo())
        );
    }

    private Order buildOrder(User user, PaymentInfo paymentInfo, OrderRequest orderRequest) {
        final var orderBuilder = orderRequest.toOrder();
        final var fullPrice = shoppingCartService.getPrice(user.getShoppingCart());
        orderBuilder.setUser(user);
        orderBuilder.setPaymentInfo(paymentInfo);
        orderBuilder.setStatus(Order.OrderStatus.PENDING_PAYMENT);
        orderBuilder.setOrderAmount(fullPrice);
        orderBuilder.setCreatedAt(Instant.now());
        orderBuilder.setPromoCode(user.getShoppingCart().getPromoCode());

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

    public InputStream getReceipt(Long orderId) {
        var user = userService.getCurrentUser();
        return getReceipt(orderId, user);
    }

    public InputStream getReceipt(Long orderId, User user) {
        final var order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Заказ не найден")
        );

        if (!Objects.equals(order.getUser().getId(), user.getId()))
            throw new OrderNotAvailableException("Пользователь не может получить чек чужого заказа");

        if (order.getStatus() == Order.OrderStatus.PENDING_PAYMENT
                || order.getStatus() == Order.OrderStatus.CANCELLED
                || order.getStatus() == Order.OrderStatus.CANCELLED_TIMEOUT)
            throw new OrderNotAvailableException("Заказ был отменён или не оплачен");

        try {
            return minioService.downloadFile(user.getId() + "_" + orderId + ".pdf");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void successfulPayment(Long orderId) {
        final var order = orderRepository.findByIdWithItems(orderId).orElseThrow(
                () -> new OrderNotFoundException("Заказ не найден, вы зря оплатили :(")
        );
        order.setStatus(Order.OrderStatus.PROCESSING);
        orderRepository.save(order);

        final var receiptDto = ReceiptDto.fromOrder(order);
        jmsService.sendOrderMessage(receiptDto);
    }
}
