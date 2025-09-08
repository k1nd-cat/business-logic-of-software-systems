package io.blss.labs.worker.courier;

import io.blss.labs.config.DataInitializer;
import io.blss.labs.entity.User;
import io.blss.labs.exception.InvalidPermissionException;
import io.blss.labs.service.AuthService;
import io.blss.labs.service.CourierService;
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
@ExternalTaskSubscription("accepted-orders")
public class AcceptedOrdersWorker implements ExternalTaskHandler {
    private final UserService userService;
    private final AuthService authService;
    private final CourierService courierService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            String permission = DataInitializer.PERM_VIEW_ACCEPTED_ORDERS;
            String token = externalTask.getVariable("token");
            User user = token != null ? userService.getCurrentUser(token) : null;
            var hasPermission = authService.checkPermission(permission, user);
            if (!hasPermission || user == null)
                throw new InvalidPermissionException("У пользователя нет прав на данное действие");

            var orders = courierService.getSelectedOrders(Pageable.unpaged(), user);
            Map<String, Object> variables = Collections.singletonMap("orders", orders);
            externalTaskService.complete(externalTask, variables);
        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "Error",  e.getMessage());
        }
    }
}
