package br.com.desafio.compradireta.adapter.gateway.amqp.notificacao;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import br.com.desafio.compradireta.adapter.entrypoint.filter.CorrelationIdFilter;
import br.com.desafio.compradireta.application.config.RabbitMQProperties;
import br.com.desafio.compradireta.domain.interfaces.event.INotificacaoPublicacaoProducer;
import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event.CompraPublicadaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RabbitMQNotificacaoPublicacaoProducerImpl implements INotificacaoPublicacaoProducer {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;

    @Override
    public void publicar(CompraPublicadaEvent evento) {
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        if (correlationId == null || correlationId.trim().isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }
        final String finalCorrelationId = correlationId;

        log.info("Publicando evento de compra direta publicada: compraId={}, numero={}",
                evento.getCompraDiretaId(), evento.getNumero());

        rabbitTemplate.convertAndSend(properties.getExchange(), properties.getRoutingKey(), evento, message -> {
            message.getMessageProperties().setHeader(CorrelationIdFilter.CORRELATION_ID_HEADER, finalCorrelationId);
            message.getMessageProperties().setCorrelationId(finalCorrelationId);
            return message;
        });
    }
}
