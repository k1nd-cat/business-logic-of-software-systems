package io.blss.labs.worker.shoppingcart;

import io.blss.labs.config.DataInitializer;
import io.blss.labs.dto.CartItemResponse;
import io.blss.labs.entity.User;
import io.blss.labs.exception.InvalidPermissionException;
import io.blss.labs.service.AuthService;
import io.blss.labs.service.ShoppingCartService;
import io.blss.labs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ExternalTaskSubscription("shopping-cart-content")
public class ShoppingCartContentWorker implements ExternalTaskHandler {
    private final UserService userService;
    private final AuthService authService;
    private final ShoppingCartService shoppingCartService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String permission = DataInitializer.PERM_MANAGE_CART;
        try {
            String token = externalTask.getVariable("token");
            User user = token != null ? userService.getCurrentUser(token) : null;
            var hasPermission = authService.checkPermission(permission, user);
            if (!hasPermission || user == null)
                throw new InvalidPermissionException("У пользователя нет прав на данное действие");

            var shoppingCartItems = shoppingCartService.getProductsInCart(Pageable.unpaged(), user.getShoppingCart()).getContent();
            Map<String, Object> shoppingCartContent = Collections.singletonMap("shoppingCartContent", shoppingCartItems);
            externalTaskService.complete(externalTask, shoppingCartContent);
        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "Error",  e.getMessage());
        }
    }
}
