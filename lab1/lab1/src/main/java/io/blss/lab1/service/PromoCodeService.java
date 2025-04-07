package io.blss.lab1.service;

import io.blss.lab1.entity.Order;
import io.blss.lab1.entity.PromoCode;
import io.blss.lab1.entity.ShoppingCart;
import io.blss.lab1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final UserService userService;

    private final OrderRepository orderRepository;

    public Boolean checkForValidityOrder(PromoCode promoCode, Order order) {
//        TODO
        return null;
    }

    public Boolean checkForValidityCart(PromoCode promoCode, ShoppingCart cart) {
        if (cart.getPromoCode() == null) return false;
        final var user = userService.getCurrentUser();
        final var promoCodeStartDate = promoCode.getStartDate();
        final var promoCodeEndDate = promoCode.getEndDate();

        final var currentTime = LocalDateTime.now();
        if (currentTime.isBefore(promoCodeStartDate) || currentTime.isAfter(promoCodeEndDate)) {
            return false;
        }

        final var ordersCount = orderRepository.countOrdersByUser(user);
        if (promoCode.getCondition() == PromoCode.Condition.FIRST_ORDER && ordersCount > 0)
            return false;

        return true;
    }
}
