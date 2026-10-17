package br.com.desafio.compradireta.adapter.entrypoint.amqp.notificacao;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event.CompraPublicadaEvent;
import br.com.desafio.compradireta.domain.usecase.notificacao.publicar.NotificarFornecedoresPublicacaoUseCase;

@ExtendWith(MockitoExtension.class)
class NotificacaoPublicacaoListenerTest {

    @Mock
    private NotificarFornecedoresPublicacaoUseCase notificarFornecedoresPublicacaoUseCase;

    @InjectMocks
    private NotificacaoPublicacaoListener listener;

    @Test
    void deveDelegarExecucaoParaUseCaseAoReceberEventoTest() {
        CompraPublicadaEvent evento = CompraPublicadaEvent.builder()
                .compraDiretaId(10L)
                .numero("CD-0001/2026")
                .fornecedoresConvidadosIds(List.of(1L, 2L))
                .build();

        listener.processar(evento);

        verify(notificarFornecedoresPublicacaoUseCase).executar(10L);
    }
}
