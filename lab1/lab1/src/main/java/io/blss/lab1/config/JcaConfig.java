package io.blss.lab1.config;

import io.blss.lab1.robokassa.RobokassaClient;
import io.blss.lab1.robokassa.impl.RobokassaClientBuilder;
import io.blss.lab1.robokassa.jca.RobokassaManagedConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jca.support.LocalConnectionFactoryBean;
import jakarta.resource.cci.ConnectionFactory;

@Configuration
public class JcaConfig {

    @Bean
    public RobokassaClient robokassaClient() {
        return new RobokassaClientBuilder()
                .merchantLogin("login")
                .merchantPassword("password1")
                .checkPassword("password2")
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
