package io.blss.labs.worker.products;

import io.blss.labs.config.DataInitializer;
import io.blss.labs.dto.ProductResponse;
import io.blss.labs.entity.Product;
import io.blss.labs.entity.User;
import io.blss.labs.exception.InvalidPermissionException;
import io.blss.labs.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.atn.LexerPopModeAction;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@ExternalTaskSubscription("product-by-id")
public class ProductByIdWorker implements ExternalTaskHandler {
    private final ProductService productService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        try {
            String permission = null;
            Long productId = externalTask.getVariable("productId");
            var product = productService.getProductById(productId);
            List<ProductResponse> productsList = new ArrayList<>();
            productsList.add(product);
            Map<String, Object> variables = Collections.singletonMap("showList", productsList);
            externalTaskService.complete(externalTask, variables);
        } catch (Exception e) {
            externalTaskService.handleBpmnError(externalTask, "error", e.getMessage());
        }
    }
}
