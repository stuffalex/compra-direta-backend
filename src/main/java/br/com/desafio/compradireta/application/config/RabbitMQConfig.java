package br.com.desafio.compradireta.application.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RabbitMQProperties properties;

    @Bean
    public DirectExchange compraDiretaExchange() {
        return new DirectExchange(properties.getExchange());
    }

    @Bean
    public DirectExchange compraDiretaDlx() {
        return new DirectExchange(properties.getDlx());
    }

    @Bean
    public Queue notificacaoQueue() {
        return QueueBuilder.durable(properties.getQueue())
                .withArgument("x-dead-letter-exchange", properties.getDlx())
                .withArgument("x-dead-letter-routing-key", properties.getDlqRoutingKey())
                .build();
    }

    @Bean
    public Queue notificacaoDlq() {
        return QueueBuilder.durable(properties.getDlq()).build();
    }

    @Bean
    public Binding bindingNotificacaoQueue(Queue notificacaoQueue, DirectExchange compraDiretaExchange) {
        return BindingBuilder.bind(notificacaoQueue)
                .to(compraDiretaExchange)
                .with(properties.getRoutingKey());
    }

    @Bean
    public Binding bindingNotificacaoDlq(Queue notificacaoDlq, DirectExchange compraDiretaDlx) {
        return BindingBuilder.bind(notificacaoDlq)
                .to(compraDiretaDlx)
                .with(properties.getDlqRoutingKey());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}
