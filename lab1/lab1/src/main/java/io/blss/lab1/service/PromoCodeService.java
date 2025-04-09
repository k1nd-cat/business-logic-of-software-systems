package io.blss.lab1.service;

import io.blss.lab1.entity.PromoCode;
import io.blss.lab1.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final UserService userService;

    private final OrderRepository orderRepository;

    public Boolean checkForValidityCart(PromoCode promoCode) {
        final var user = userService.getCurrentUser();
        final var promoCodeStartDate = promoCode.getStartDate();
        final var promoCodeEndDate = promoCode.getEndDate();

        final var currentTime = new Date();
        if (currentTime.before(promoCodeStartDate) || currentTime.after(promoCodeEndDate)) {
            return false;
        }

        final var ordersCount = orderRepository.countOrdersByUser(user);
        if (promoCode.getCondition() == PromoCode.Condition.FIRST_ORDER && ordersCount > 0)
            return false;

        return true;
    }
}
