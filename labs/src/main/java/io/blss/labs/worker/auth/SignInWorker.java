package io.blss.labs.worker.auth;

import io.blss.labs.config.DataInitializer;
import io.blss.labs.dto.AuthRequest;
import io.blss.labs.dto.AuthResponse;
import io.blss.labs.service.AuthService;
import io.blss.labs.service.UserService;
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
@ExternalTaskSubscription("sign-in")
public class SignInWorker implements ExternalTaskHandler {
    private final AuthService authService;
    private final UserService userService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String permission = null;

        String username = externalTask.getVariable("username");
        String password = externalTask.getVariable("password");
        var authRequest = new AuthRequest();
        authRequest.setUsername(username);
        authRequest.setPassword(password);
        try {
            AuthResponse response = authService.signIn(authRequest);
            var user = userService.getCurrentUser(response.getToken());
            var role = user.getRole();
            Map<String, Object> variables = new HashMap<>();
            variables.put("role", role.getName().equals("USER_ROLE") ? "Пользователь" : "Курьер");
            variables.put("token", response.getToken());
            variables.put("isCourier", !role.getName().equals("USER_ROLE"));
            externalTaskService.setVariables(externalTask, variables);
            externalTaskService.complete(externalTask, variables);
        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "authError", e.getMessage());
        }
    }
}
