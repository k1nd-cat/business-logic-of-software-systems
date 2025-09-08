package io.blss.labs.worker.categories;

import io.blss.labs.dto.ProductCategoryResponse;
import io.blss.labs.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ExternalTaskSubscription("categories")
public class CategoriesWorker implements ExternalTaskHandler {

    private final ProductCategoryService productCategoryService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String permission = null;
        try {
            List<ProductCategoryResponse> categories = productCategoryService.getProductCategories(Pageable.unpaged()).getContent();
            Map<String, Object> variables = Collections.singletonMap("categories", categories);
            externalTaskService.complete(externalTask, variables);
        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "error", e.getMessage());
        }
    }
}
