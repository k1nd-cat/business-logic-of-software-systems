package io.blss.labs.worker;

import io.blss.labs.scheduler.OrderTimeoutScheduler;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ExternalTaskSubscription("timer-task")
public class SchedulerWorker implements ExternalTaskHandler {

    private final OrderTimeoutScheduler orderTimeoutScheduler;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        orderTimeoutScheduler.cancelOverduePendingPaymentOrders();
        externalTaskService.complete(externalTask);
    }
}
