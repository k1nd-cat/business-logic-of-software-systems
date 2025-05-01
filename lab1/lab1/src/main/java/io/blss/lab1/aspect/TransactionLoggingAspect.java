package io.blss.lab1.aspect;

import io.blss.lab1.repository.CartItemRepository;
import io.blss.lab1.repository.PaymentInfoRepository;
import io.blss.lab1.repository.ShoppingCartRepository;
import io.blss.lab1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TransactionLoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(TransactionLoggingAspect.class);

    private final ShoppingCartRepository shoppingCartRepository;

    private final CartItemRepository cartItemRepository;

    private final UserService userService;

    private final PaymentInfoRepository paymentInfoRepository;

    @Pointcut("execution(public * io.blss.lab1.service.OrderService.makeOrder(..))")
    public void makeOrderExecution() {}

    @Before("makeOrderExecution()")
    public void logBeforeTransactionBegin(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info(">>> TRANSACTION BEGIN: Transaction for method [{}] begin.", methodName);
        printBasicInfo();
    }

    @AfterReturning("makeOrderExecution()")
    public void logAfterTransactionCommit(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info(">>> TRANSACTION SUCCEEDED: Transaction for method [{}] committed successfully.", methodName);
        printBasicInfo();
    }

    @AfterThrowing(pointcut = "makeOrderExecution()", throwing = "ex")
    public void logAfterTransactionRollback(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error(">>> TRANSACTION FAILED: Transaction for method [{}] rolled back due to exception: {} - {}",
                methodName, ex.getClass().getSimpleName(), ex.getMessage());
        printBasicInfo();
    }
    
    @After("makeOrderExecution()")
    public void lofAfterMethodEnd(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.error(">>> TRANSACTION END: Transaction for method [{}] end", methodName);
        printBasicInfo();
    }

    private void printBasicInfo() {
        final var user = userService.getCurrentUser();
//        final var actualPaymentInfo = paymentInfoRepository.findByUserAndIsActual(user, true).orElse(null);
//        log.info("Actual card number: {}", actualPaymentInfo != null ? actualPaymentInfo.getCardNumber() : "nothing");
    }
}
