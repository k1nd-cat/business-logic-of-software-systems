package io.blss.lab1.scheduler;

import io.blss.lab1.entity.Order;
import io.blss.lab1.repository.OrderRepository;
import io.blss.lab1.service.OrderService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderTimeoutScheduler {
    private static final Logger log = LoggerFactory.getLogger(OrderTimeoutScheduler.class);
    private static final int CANCELLATION_TIMEOUT_MINUTES = 10;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelOverduePendingPaymentOrders() {
        Order.OrderStatus targetStatus = Order.OrderStatus.PENDING_PAYMENT;

        log.info("Планировщик: Запуск проверки просроченных заказов в статусе '{}'", targetStatus);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -CANCELLATION_TIMEOUT_MINUTES);
        Instant timeLimit = cal.getTime().toInstant();

        List<Order> overdueOrders = orderRepository.findByStatusAndCreatedAtBefore(
                targetStatus,
                timeLimit
        );

        if (overdueOrders.isEmpty()) {
            log.info("Планировщик: Просроченные заказы в статусе '{}' не найдены.", targetStatus);
            return;
        }

        log.info("Планировщик: Найдено {} просроченных заказов для отмены.", overdueOrders.size());

        for (Order order : overdueOrders) {
            log.info("Планировщик: Отмена заказа ID: {}, созданного в: {}", order.getId(), order.getCreatedAt());
            order.setStatus(Order.OrderStatus.CANCELLED_TIMEOUT);
            orderService.cancelOrder(order.getId());
        }
        log.info("Планировщик: Завершено. Отменено {} просроченных заказов.", overdueOrders.size());
    }
}
