package io.blss.lab1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.blss.lab1.config.JmsConfig;
import io.blss.lab1.dto.ReceiptDto;
import io.blss.lab1.dto.RobokassaRequestDto;
import io.blss.lab1.entity.Order;
import jakarta.jms.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JmsService {
    private final JmsTemplate jmsTemplate;

    public void sendOrderMessage(ReceiptDto receiptDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(receiptDto);
            jmsTemplate.convertAndSend(JmsConfig.RECEIPT_GENERATOR_QUEUE, message);
            System.out.println("Sent message: " + message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String getPaymentLink(RobokassaRequestDto requestDto) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestJson = objectMapper.writeValueAsString(requestDto);

            String robokassaUrl = jmsTemplate.execute(session -> {
                Destination requestQueue = session.createQueue(JmsConfig.ROBOKASSA_REQUEST_QUEUE);

                TemporaryQueue replyQueue = session.createTemporaryQueue();

                TextMessage requestMessage = session.createTextMessage(requestJson);
                requestMessage.setJMSReplyTo(replyQueue);

                try (MessageProducer producer = session.createProducer(requestQueue)) {
                    producer.send(requestMessage);

                    String selector = "JMSCorrelationID = '" + requestMessage.getJMSMessageID() + "'";
                    try (MessageConsumer consumer = session.createConsumer(replyQueue, selector)) {
                        Message replyMessage = consumer.receive(5000);
                        if (replyMessage instanceof TextMessage) {
                            return ((TextMessage) replyMessage).getText();
                        }
                        return null;
                    }
                }
            }, true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
