package br.com.desafio.compradireta.adapter.gateway.outbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.desafio.compradireta.application.config.RabbitMQProperties;
import br.com.desafio.compradireta.domain.constant.StatusOutbox;
import br.com.desafio.compradireta.domain.entity.entities.OutboxEvento;
import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event.CompraPublicadaEvent;

@ExtendWith(MockitoExtension.class)
class OutboxRelayWorkerTest {

    @Mock
    private OutboxJpaRepository outboxRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private RabbitMQProperties properties;
    private ObjectMapper objectMapper;
    private OutboxRelayWorker worker;

    @BeforeEach
    void setUp() {
        properties = new RabbitMQProperties();
        properties.setExchange("compra-direta.exchange");
        properties.setRoutingKey("compra-direta.publicada");
        objectMapper = new ObjectMapper();

        worker = new OutboxRelayWorker(outboxRepository, rabbitTemplate, properties, objectMapper);
    }

    @Test
    void deveDespacharEventoPendenteParaRabbitMQEAtualizarStatusTest() throws Exception {
        CompraPublicadaEvent evento = CompraPublicadaEvent.builder()
                .compraDiretaId(20L)
                .numero("CD-0020/2026")
                .fornecedoresConvidadosIds(List.of(1L, 2L))
                .build();

        String payload = objectMapper.writeValueAsString(evento);
        OutboxEvento outboxEvento = OutboxEvento.builder()
                .id(1L)
                .aggregateType("COMPRA_DIRETA")
                .aggregateId(20L)
                .eventType("COMPRA_PUBLICADA")
                .payload(payload)
                .correlationId("corr-123")
                .status(StatusOutbox.PENDENTE)
                .criadoEm(LocalDateTime.now())
                .tentativas(0)
                .build();

        when(outboxRepository.findByStatusOrderByCriadoEmAsc(StatusOutbox.PENDENTE, PageRequest.of(0, 50)))
                .thenReturn(List.of(outboxEvento));

        worker.processarEventosPendentes();

        verify(rabbitTemplate).convertAndSend(eq("compra-direta.exchange"), eq("compra-direta.publicada"), any(CompraPublicadaEvent.class), any(MessagePostProcessor.class));
        assertEquals(StatusOutbox.PROCESSADO, outboxEvento.getStatus());
        assertNotNull(outboxEvento.getProcessadoEm());
        verify(outboxRepository).save(outboxEvento);
    }

    @Test
    void deveIncrementarTentativasQuandoFalhaNoEnvioTest() throws Exception {
        CompraPublicadaEvent evento = CompraPublicadaEvent.builder()
                .compraDiretaId(20L)
                .numero("CD-0020/2026")
                .build();

        String payload = objectMapper.writeValueAsString(evento);
        OutboxEvento outboxEvento = OutboxEvento.builder()
                .id(2L)
                .aggregateType("COMPRA_DIRETA")
                .aggregateId(20L)
                .eventType("COMPRA_PUBLICADA")
                .payload(payload)
                .status(StatusOutbox.PENDENTE)
                .criadoEm(LocalDateTime.now())
                .tentativas(2)
                .build();

        when(outboxRepository.findByStatusOrderByCriadoEmAsc(StatusOutbox.PENDENTE, PageRequest.of(0, 50)))
                .thenReturn(List.of(outboxEvento));

        doThrow(new RuntimeException("RabbitMQ connection refused"))
                .when(rabbitTemplate).convertAndSend(eq("compra-direta.exchange"), eq("compra-direta.publicada"), any(CompraPublicadaEvent.class), any(MessagePostProcessor.class));

        worker.processarEventosPendentes();

        assertEquals(3, outboxEvento.getTentativas());
        assertEquals(StatusOutbox.FALHA, outboxEvento.getStatus());
        assertEquals("RabbitMQ connection refused", outboxEvento.getErro());
        verify(outboxRepository).save(outboxEvento);
    }

    @Test
    void deveNaoFazerNadaQuandoNaoHaEventosPendentesTest() {
        when(outboxRepository.findByStatusOrderByCriadoEmAsc(StatusOutbox.PENDENTE, PageRequest.of(0, 50)))
                .thenReturn(Collections.emptyList());

        worker.processarEventosPendentes();

        verify(rabbitTemplate, never()).convertAndSend(any(), any(), any(Object.class));
    }
}
