package io.blss.lab1.config;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import jakarta.jms.ConnectionFactory;

@Configuration
@EnableJms
public class JmsConfig {

    public static final String ROBOKASSA_REQUEST_QUEUE = "robokassaPaymentRequestQueue";

    public static final String RECEIPT_GENERATOR_QUEUE = "generate_receipt_queue";

    @Bean
    public ConnectionFactory jmsConnectionFactory(
            @Value("${spring.rabbitmq.host}") String host,
            @Value("${spring.rabbitmq.port}") int port,
            @Value("${spring.rabbitmq.username}") String username,
            @Value("${spring.rabbitmq.password}") String password) {

        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
//        jmsTemplate.setDefaultDestinationName("generate_receipt_queue");
        return jmsTemplate;
    }
}
