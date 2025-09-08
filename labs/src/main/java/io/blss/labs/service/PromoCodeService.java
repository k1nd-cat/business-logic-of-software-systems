package io.blss.labs.service;

import io.blss.labs.entity.PromoCode;
import io.blss.labs.entity.User;
import io.blss.labs.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class PromoCodeService {

    private final UserService userService;

    private final OrderRepository orderRepository;

    public Boolean checkForValidityCart(PromoCode promoCode) {
        var user = userService.getCurrentUser();
        return checkForValidityCart(promoCode, user);
        
    }

    public Boolean checkForValidityCart(PromoCode promoCode, User user) {
        final var promoCodeStartDate = promoCode.getStartDate();
        final var promoCodeEndDate = promoCode.getEndDate();

        final var currentTime = new Date();
        if (currentTime.before(promoCodeStartDate) || currentTime.after(promoCodeEndDate)) {
            return false;
        }

        final var ordersCount = orderRepository.countOrdersByUser(user);
        return promoCode.getCondition() != PromoCode.Condition.FIRST_ORDER || ordersCount <= 0;
    }
}
