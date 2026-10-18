package br.com.desafio.compradireta.adapter.gateway.outbox;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafio.compradireta.adapter.entrypoint.filter.CorrelationIdFilter;
import br.com.desafio.compradireta.application.config.RabbitMQProperties;
import br.com.desafio.compradireta.domain.constant.StatusOutbox;
import br.com.desafio.compradireta.domain.entity.entities.OutboxEvento;
import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event.CompraPublicadaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelayWorker {

    private final OutboxJpaRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties properties;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void processarEventosPendentes() {
        List<OutboxEvento> pendentes = outboxRepository.findByStatusOrderByCriadoEmAsc(
                StatusOutbox.PENDENTE, PageRequest.of(0, 50));

        if (pendentes.isEmpty()) {
            return;
        }

        log.debug("Processando {} eventos pendentes na outbox", pendentes.size());

        for (OutboxEvento eventoOutbox : pendentes) {
            despacharEvento(eventoOutbox);
        }
    }

    private void despacharEvento(OutboxEvento eventoOutbox) {
        String correlationId = eventoOutbox.getCorrelationId();
        if (correlationId != null) {
            MDC.put(CorrelationIdFilter.CORRELATION_ID_MDC_KEY, correlationId);
        }

        try {
            CompraPublicadaEvent evento = objectMapper.readValue(eventoOutbox.getPayload(), CompraPublicadaEvent.class);

            rabbitTemplate.convertAndSend(properties.getExchange(), properties.getRoutingKey(), evento, message -> {
                if (correlationId != null) {
                    message.getMessageProperties().setHeader(CorrelationIdFilter.CORRELATION_ID_HEADER, correlationId);
                    message.getMessageProperties().setCorrelationId(correlationId);
                }
                return message;
            });

            eventoOutbox.setStatus(StatusOutbox.PROCESSADO);
            eventoOutbox.setProcessadoEm(LocalDateTime.now());
            outboxRepository.save(eventoOutbox);

            log.info("Evento outbox despachado com sucesso para RabbitMQ: id={}, aggregateId={}",
                    eventoOutbox.getId(), eventoOutbox.getAggregateId());
        } catch (Exception e) {
            log.error("Erro ao despachar evento outbox para RabbitMQ: id={}", eventoOutbox.getId(), e);
            eventoOutbox.setTentativas(eventoOutbox.getTentativas() + 1);
            eventoOutbox.setErro(e.getMessage());
            if (eventoOutbox.getTentativas() >= 3) {
                eventoOutbox.setStatus(StatusOutbox.FALHA);
            }
            outboxRepository.save(eventoOutbox);
        } finally {
            MDC.remove(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        }
    }
}
