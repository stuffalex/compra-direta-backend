package br.com.desafio.compradireta.adapter.gateway.outbox;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.MDC;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafio.compradireta.adapter.entrypoint.filter.CorrelationIdFilter;
import br.com.desafio.compradireta.domain.constant.StatusOutbox;
import br.com.desafio.compradireta.domain.entity.entities.OutboxEvento;
import br.com.desafio.compradireta.domain.interfaces.event.INotificacaoPublicacaoProducer;
import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event.CompraPublicadaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OutboxNotificacaoPublicacaoProducerImpl implements INotificacaoPublicacaoProducer {

    private final OutboxJpaRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void publicar(CompraPublicadaEvent evento) {
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        if (correlationId == null || correlationId.trim().isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        try {
            String payload = objectMapper.writeValueAsString(evento);
            OutboxEvento outboxEvento = OutboxEvento.builder()
                    .aggregateType("COMPRA_DIRETA")
                    .aggregateId(evento.getCompraDiretaId())
                    .eventType("COMPRA_PUBLICADA")
                    .payload(payload)
                    .correlationId(correlationId)
                    .status(StatusOutbox.PENDENTE)
                    .criadoEm(LocalDateTime.now())
                    .build();

            outboxRepository.save(outboxEvento);
            log.info("Evento gravado na tabela outbox com sucesso: id={}, aggregateId={}",
                    outboxEvento.getId(), evento.getCompraDiretaId());
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar evento para outbox: compraId={}", evento.getCompraDiretaId(), e);
            throw new RuntimeException("Falha na serialização do evento outbox", e);
        }
    }
}
