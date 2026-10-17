package br.com.desafio.compradireta.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq.topology")
public class RabbitMQProperties {

    private String exchange;
    private String routingKey;
    private String queue;
    private String dlx;
    private String dlqRoutingKey;
    private String dlq;
}
