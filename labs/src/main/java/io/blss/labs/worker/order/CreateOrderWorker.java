package io.blss.labs.worker.order;

import io.blss.labs.config.DataInitializer;
import io.blss.labs.dto.OrderRequest;
import io.blss.labs.entity.Order;
import io.blss.labs.entity.User;
import io.blss.labs.exception.InvalidPermissionException;
import io.blss.labs.service.AuthService;
import io.blss.labs.service.OrderService;
import io.blss.labs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@ExternalTaskSubscription("create-order")
public class CreateOrderWorker implements ExternalTaskHandler {
    private final UserService userService;
    private final AuthService authService;
    private final OrderService orderService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String permission = DataInitializer.PERM_MANAGE_ORDERS;
        try {
            String token = externalTask.getVariable("token");
            User user = token != null ? userService.getCurrentUser(token) : null;
            var hasPermission = authService.checkPermission(permission, user);
            if (!hasPermission || user == null)
                throw new InvalidPermissionException("У пользователя нет прав на данное действие");
            

            String number = externalTask.getVariable("phoneNumber");
            String address = externalTask.getVariable("userAddress");
            
            OrderRequest orderRequest = OrderRequest.builder()
                    .number(number)
                    .address(address)
                    .deliveryType(Order.DeliveryType.COURIER)
                    .cardNumber("3284 3245 0983 1234")
                    .build();
            String robokassaUrl = orderService.makeOrder(orderRequest, user);
            externalTaskService.complete(externalTask, Collections.singletonMap("robokassaUrl", robokassaUrl));

        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "error", e.getMessage());
        }
    }
}
