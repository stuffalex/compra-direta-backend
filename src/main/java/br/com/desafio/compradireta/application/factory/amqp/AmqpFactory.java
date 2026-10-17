package br.com.desafio.compradireta.application.factory.amqp;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.desafio.compradireta.adapter.gateway.amqp.notificacao.RabbitMQNotificacaoPublicacaoProducerImpl;
import br.com.desafio.compradireta.application.config.RabbitMQProperties;
import br.com.desafio.compradireta.domain.interfaces.event.INotificacaoPublicacaoProducer;

@Configuration
public class AmqpFactory {

    @Bean
    public INotificacaoPublicacaoProducer notificacaoPublicacaoProducer(
            RabbitTemplate rabbitTemplate,
            RabbitMQProperties properties) {
        return new RabbitMQNotificacaoPublicacaoProducerImpl(rabbitTemplate, properties);
    }
}
