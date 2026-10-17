package br.com.desafio.compradireta.adapter.entrypoint.amqp.notificacao;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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
    public void processar(CompraPublicadaEvent evento) {
        log.info("Recebido evento de publicação para compra: compraId={}, numero={}",
                evento.getCompraDiretaId(), evento.getNumero());
        notificarFornecedoresPublicacaoUseCase.executar(evento.getCompraDiretaId());
        log.info("Notificações de publicação processadas com sucesso para compraId={}",
                evento.getCompraDiretaId());
    }
}
