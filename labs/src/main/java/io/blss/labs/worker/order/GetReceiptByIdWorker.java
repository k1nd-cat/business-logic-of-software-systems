package io.blss.labs.worker.order;

import io.blss.labs.config.DataInitializer;
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

import java.io.InputStream;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@ExternalTaskSubscription("get-receipt")
public class GetReceiptByIdWorker implements ExternalTaskHandler {
    private final UserService userService;
    private final AuthService authService;
    private final OrderService orderService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            String permission = DataInitializer.PERM_MANAGE_ORDERS;
            String token = externalTask.getVariable("token");
            User user = token != null ? userService.getCurrentUser(token) : null;
            var hasPermission = authService.checkPermission(permission, user);
            if (!hasPermission || user == null)
                throw new InvalidPermissionException("У пользователя нет прав на данное действие");
            
            Long orderId = externalTask.getVariable("orderId");

            try (InputStream receiptInputStream = orderService.getReceipt(orderId)) {
                byte[] receiptBytes = receiptInputStream.readAllBytes();
                externalTaskService.complete(externalTask, Collections.singletonMap("receipt", receiptBytes));
            }
        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "error", e.getMessage());
        }
    }
}
