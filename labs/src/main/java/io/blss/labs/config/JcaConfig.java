package io.blss.labs.config;

import io.blss.labs.robokassa.RobokassaClient;
import io.blss.labs.robokassa.impl.RobokassaClientBuilder;
import io.blss.labs.robokassa.jca.RobokassaManagedConnectionFactory;
import jakarta.resource.cci.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jca.support.LocalConnectionFactoryBean;

@Configuration
public class JcaConfig {

    @Bean
    public RobokassaClient robokassaClient() {
        return new RobokassaClientBuilder()
                .merchantLogin("software-business-logic-labs-itmo")
                .merchantPassword("y70nsmV8BBeNSUa8w7Ox")
                .checkPassword("FrTX3o9a0TeQyZQSG88R")
                .build();
    }

    @Bean
    public RobokassaManagedConnectionFactory managedConnectionFactory(RobokassaClient client) {
        return new RobokassaManagedConnectionFactory(client);
    }

    @Bean
    public ConnectionFactory connectionFactory(RobokassaManagedConnectionFactory mcf) throws Exception {
        LocalConnectionFactoryBean factoryBean = new LocalConnectionFactoryBean();
        factoryBean.setManagedConnectionFactory(mcf);
        factoryBean.afterPropertiesSet();
        return (ConnectionFactory) factoryBean.getObject();
    }
}
