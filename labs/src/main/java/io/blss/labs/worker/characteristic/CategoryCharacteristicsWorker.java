package io.blss.labs.worker.characteristic;

import io.blss.labs.service.FilterService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ExternalTaskSubscription("category-characteristics")
public class CategoryCharacteristicsWorker implements ExternalTaskHandler {
    private final FilterService filterService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        long categoryId = Integer.parseInt(externalTask.getVariable("categoryId"));

        var filters = filterService.getFiltersByCategoryId(categoryId);
        Map<String, Object> variables = Collections.singletonMap("filters", filters);
        externalTaskService.complete(externalTask, variables);
    }
}
