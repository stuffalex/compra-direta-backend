package br.com.desafio.compradireta.adapter.gateway.outbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafio.compradireta.adapter.entrypoint.filter.CorrelationIdFilter;
import br.com.desafio.compradireta.domain.constant.StatusOutbox;
import br.com.desafio.compradireta.domain.entity.entities.OutboxEvento;
import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event.CompraPublicadaEvent;

@ExtendWith(MockitoExtension.class)
class OutboxNotificacaoPublicacaoProducerImplTest {

    @Mock
    private OutboxJpaRepository outboxRepository;

    private ObjectMapper objectMapper;
    private OutboxNotificacaoPublicacaoProducerImpl producer;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        producer = new OutboxNotificacaoPublicacaoProducerImpl(outboxRepository, objectMapper);
    }

    @Test
    void deveGravarEventoNaTabelaOutboxComStatusPendenteTest() {
        MDC.put(CorrelationIdFilter.CORRELATION_ID_MDC_KEY, "corr-outbox-123");

        CompraPublicadaEvent evento = CompraPublicadaEvent.builder()
                .compraDiretaId(15L)
                .numero("CD-0015/2026")
                .fornecedoresConvidadosIds(List.of(1L, 2L))
                .build();

        producer.publicar(evento);

        ArgumentCaptor<OutboxEvento> captor = ArgumentCaptor.forClass(OutboxEvento.class);
        verify(outboxRepository).save(captor.capture());

        OutboxEvento salvo = captor.getValue();
        assertEquals("COMPRA_DIRETA", salvo.getAggregateType());
        assertEquals(15L, salvo.getAggregateId());
        assertEquals("COMPRA_PUBLICADA", salvo.getEventType());
        assertEquals(StatusOutbox.PENDENTE, salvo.getStatus());
        assertEquals("corr-outbox-123", salvo.getCorrelationId());
        assertNotNull(salvo.getPayload());
        assertNotNull(salvo.getCriadoEm());

        MDC.clear();
    }
}
