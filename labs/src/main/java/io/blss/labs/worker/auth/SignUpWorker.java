package io.blss.labs.worker.auth;

import io.blss.labs.dto.AuthRequest;
import io.blss.labs.dto.AuthResponse;
import io.blss.labs.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ExternalTaskSubscription("sign-up")
public class SignUpWorker implements ExternalTaskHandler {
    private final AuthService authService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String permission = null;

        String username = externalTask.getVariable("username");
        String password = externalTask.getVariable("password");
        Boolean isCourier = externalTask.getVariable("isCourier");

        var request = new AuthRequest(username, password);
        try {
            AuthResponse response = isCourier
                    ? authService.signUpCourier(request)
                    : authService.signUpUser(request);
            Map<String, Object> variables = new HashMap<>();
            variables.put("role", isCourier ? "Курьер" : "Пользователь");
            variables.put("token", response.getToken());
            variables.put("isCourier", isCourier);
            externalTaskService.setVariables(externalTask, variables);
            externalTaskService.complete(externalTask, variables);
        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "authError", e.getMessage());
        }
    }
}
