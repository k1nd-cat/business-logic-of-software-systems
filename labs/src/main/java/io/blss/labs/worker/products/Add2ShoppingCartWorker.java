package io.blss.labs.worker.products;

import io.blss.labs.config.DataInitializer;
import io.blss.labs.entity.User;
import io.blss.labs.exception.InvalidPermissionException;
import io.blss.labs.service.AuthService;
import io.blss.labs.service.ProductService;
import io.blss.labs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ExternalTaskSubscription("add-to-shopping-cart")
public class Add2ShoppingCartWorker implements ExternalTaskHandler {
    private final UserService userService;
    private final AuthService authService;
    private final ProductService productService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            String permission = DataInitializer.PERM_ADD_PRODUCT_TO_CART;
            String token = externalTask.getVariable("token");
            User user = token != null ? userService.getCurrentUser(token) : null;
            var hasPermission = authService.checkPermission(permission, user);
            if (!hasPermission || user == null)
                throw new InvalidPermissionException("У пользователя нет прав на данное действие");
            
            Long productId = externalTask.getVariable("productId");
            Long quantity = externalTask.getVariable("quantity");
            productService.addProduct2Cart(productId, quantity, user);
            externalTaskService.complete(externalTask);
        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "error", e.getMessage());
        }
    }
}
