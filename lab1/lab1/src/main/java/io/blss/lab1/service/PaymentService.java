package io.blss.lab1.service;

import io.blss.lab1.robokassa.Payment;
import io.blss.lab1.robokassa.jca.RobokassaConnection;
import jakarta.resource.ResourceException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.resource.cci.ConnectionFactory;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ConnectionFactory connectionFactory;
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public String createPaymentUrl(Payment payment) {
        // Используем try-with-resources для автоматического закрытия соединения
        try (RobokassaConnection connection = (RobokassaConnection) connectionFactory.getConnection()) {
            // Теперь у тебя есть доступ к методам твоего интерфейса RobokassaConnection
            return connection.buildSubmitUrl(payment);
        } catch (Exception e) {
            // Логируем ошибку для дальнейшего анализа
            logger.error("Failed to create Robokassa payment URL", e);
            // Пробрасываем исключение как RuntimeException или возвращаем null/пустую строку,
            // в зависимости от того, как ты хочешь обрабатывать ошибки выше по стеку.
            throw new RuntimeException("Не удалось создать ссылку на оплату", e);
        }
    }
}