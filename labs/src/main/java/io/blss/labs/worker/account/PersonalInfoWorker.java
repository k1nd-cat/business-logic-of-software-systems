package io.blss.labs.worker.account;

import io.blss.labs.config.DataInitializer;
import io.blss.labs.dto.PersonalInfoResponse;
import io.blss.labs.entity.User;
import io.blss.labs.exception.InvalidPermissionException;
import io.blss.labs.service.AuthService;
import io.blss.labs.service.PersonService;
import io.blss.labs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ExternalTaskSubscription("personal-info")
public class PersonalInfoWorker implements ExternalTaskHandler {
    private final AuthService authService;
    private final UserService userService;
    private final PersonService personService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            String permission = DataInitializer.PERM_MANAGE_PERSONAL_INFO;
            String token = externalTask.getVariable("token");
            User user = token != null ? userService.getCurrentUser(token) : null;
            var hasPermission = authService.checkPermission(permission, user);
            if (!hasPermission || user == null)
                throw new InvalidPermissionException("У пользователя нет прав на данное действие");

            PersonalInfoResponse personalInfo = personService.getPersonalInfo(user);
            Map<String, Object> variables = new HashMap<>();
            variables.put("personalInfo", personalInfo);
            externalTaskService.complete(externalTask, variables);
        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "Error",  e.getMessage());
        }
    }
}
