package br.com.desafio.compradireta.adapter.entrypoint.amqp.notificacao;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import br.com.desafio.compradireta.adapter.entrypoint.filter.CorrelationIdFilter;
import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event.CompraPublicadaEvent;
import br.com.desafio.compradireta.domain.usecase.notificacao.publicar.NotificarFornecedoresPublicacaoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoPublicacaoListener {

    private final NotificarFornecedoresPublicacaoUseCase notificarFornecedoresPublicacaoUseCase;

    @RabbitListener(queues = "${spring.rabbitmq.topology.queue}")
    public void processar(
            CompraPublicadaEvent evento,
            @Header(name = CorrelationIdFilter.CORRELATION_ID_HEADER, required = false) String correlationId) {
        if (correlationId == null || correlationId.trim().isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        MDC.put(CorrelationIdFilter.CORRELATION_ID_MDC_KEY, correlationId);
        try {
            log.info("Recebido evento de publicação para compra: compraId={}, numero={}",
                    evento.getCompraDiretaId(), evento.getNumero());
            notificarFornecedoresPublicacaoUseCase.executar(evento.getCompraDiretaId());
            log.info("Notificações de publicação processadas com sucesso para compraId={}",
                    evento.getCompraDiretaId());
        } finally {
            MDC.remove(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        }
    }

    public void processar(CompraPublicadaEvent evento) {
        processar(evento, null);
    }
}
