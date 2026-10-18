package br.com.desafio.compradireta.application.factory.amqp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafio.compradireta.adapter.gateway.outbox.OutboxJpaRepository;
import br.com.desafio.compradireta.adapter.gateway.outbox.OutboxNotificacaoPublicacaoProducerImpl;
import br.com.desafio.compradireta.domain.interfaces.event.INotificacaoPublicacaoProducer;

@Configuration
public class AmqpFactory {

    @Bean
    public INotificacaoPublicacaoProducer notificacaoPublicacaoProducer(
            OutboxJpaRepository outboxRepository,
            ObjectMapper objectMapper) {
        return new OutboxNotificacaoPublicacaoProducerImpl(outboxRepository, objectMapper);
    }
}
