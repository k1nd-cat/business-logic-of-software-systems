package io.blss.labs.worker.products;

import io.blss.labs.dto.ProductResponse;
import io.blss.labs.service.ProductService;
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
@ExternalTaskSubscription("products-list")
public class ProductsListWorker implements ExternalTaskHandler {
    private final ProductService productService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        String permission = null;
        try {
            List<ProductResponse> productsList = productService.getAllProducts(Pageable.unpaged()).getContent();
            Map<String, Object> variables = Collections.singletonMap("showList", productsList);
            externalTaskService.complete(externalTask, variables);
        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "error", e.getMessage());
        }
    }
}
