package br.com.desafio.compradireta.adapter.gateway.amqp.notificacao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import br.com.desafio.compradireta.adapter.entrypoint.filter.CorrelationIdFilter;
import br.com.desafio.compradireta.application.config.RabbitMQProperties;
import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event.CompraPublicadaEvent;

@ExtendWith(MockitoExtension.class)
class RabbitMQNotificacaoPublicacaoProducerImplTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    private RabbitMQProperties properties;
    private RabbitMQNotificacaoPublicacaoProducerImpl producer;

    @BeforeEach
    void setUp() {
        properties = new RabbitMQProperties();
        properties.setExchange("compra-direta.exchange");
        properties.setRoutingKey("compra-direta.publicada");

        producer = new RabbitMQNotificacaoPublicacaoProducerImpl(rabbitTemplate, properties);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void devePublicarMensagemComExchangeERoutingKeyConfiguradosTest() {
        CompraPublicadaEvent evento = CompraPublicadaEvent.builder()
                .compraDiretaId(10L)
                .numero("CD-0001/2026")
                .fornecedoresConvidadosIds(List.of(1L, 2L))
                .build();

        producer.publicar(evento);

        verify(rabbitTemplate).convertAndSend(eq("compra-direta.exchange"), eq("compra-direta.publicada"), eq(evento), any(MessagePostProcessor.class));
    }

    @Test
    void devePropagarCorrelationIdNoHeaderDaMensagemAMQPTest() {
        String correlationIdEsperado = "teste-corr-123";
        MDC.put(CorrelationIdFilter.CORRELATION_ID_MDC_KEY, correlationIdEsperado);

        CompraPublicadaEvent evento = CompraPublicadaEvent.builder()
                .compraDiretaId(10L)
                .numero("CD-0001/2026")
                .fornecedoresConvidadosIds(List.of(1L))
                .build();

        ArgumentCaptor<MessagePostProcessor> captor = ArgumentCaptor.forClass(MessagePostProcessor.class);

        producer.publicar(evento);

        verify(rabbitTemplate).convertAndSend(eq("compra-direta.exchange"), eq("compra-direta.publicada"), eq(evento), captor.capture());

        Message message = new Message(new byte[0], new MessageProperties());
        Message processedMessage = captor.getValue().postProcessMessage(message);

        assertEquals(correlationIdEsperado, processedMessage.getMessageProperties().getHeader(CorrelationIdFilter.CORRELATION_ID_HEADER));
        assertEquals(correlationIdEsperado, processedMessage.getMessageProperties().getCorrelationId());
    }
}
