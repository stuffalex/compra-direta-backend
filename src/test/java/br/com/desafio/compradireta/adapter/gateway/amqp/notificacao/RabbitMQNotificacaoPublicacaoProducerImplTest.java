package br.com.desafio.compradireta.adapter.gateway.amqp.notificacao;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

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

    @Test
    void devePublicarMensagemComExchangeERoutingKeyConfiguradosTest() {
        CompraPublicadaEvent evento = CompraPublicadaEvent.builder()
                .compraDiretaId(10L)
                .numero("CD-0001/2026")
                .fornecedoresConvidadosIds(List.of(1L, 2L))
                .build();

        producer.publicar(evento);

        verify(rabbitTemplate).convertAndSend("compra-direta.exchange", "compra-direta.publicada", evento);
    }
}
