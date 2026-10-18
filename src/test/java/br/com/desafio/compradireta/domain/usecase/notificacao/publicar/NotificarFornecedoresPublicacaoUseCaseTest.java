package br.com.desafio.compradireta.domain.usecase.notificacao.publicar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.desafio.compradireta.domain.constant.StatusNotificacao;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDiretaFornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.NotificacaoFornecedor;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.INotificacaoFornecedorDataProvider;

@ExtendWith(MockitoExtension.class)
class NotificarFornecedoresPublicacaoUseCaseTest {

    @Mock
    private ICompraDiretaDataProvider compraDiretaDataProvider;

    @Mock
    private INotificacaoFornecedorDataProvider notificacaoFornecedorDataProvider;

    @Captor
    private ArgumentCaptor<NotificacaoFornecedor> notificacaoCaptor;

    private NotificarFornecedoresPublicacaoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = NotificarFornecedoresPublicacaoUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .notificacaoFornecedorDataProvider(notificacaoFornecedorDataProvider)
                .build();
    }

    @Test
    void deveNotificarFornecedoresQuandoNaoForamNotificadosAindaTest() {
        Long compraId = 1L;
        Fornecedor f1 = Fornecedor.builder().id(10L).razaoSocial("Fornecedor 1").build();
        Fornecedor f2 = Fornecedor.builder().id(20L).razaoSocial("Fornecedor 2").build();

        CompraDireta compraDireta = CompraDireta.builder().id(compraId).build();
        compraDireta.setFornecedoresConvidados(List.of(
                CompraDiretaFornecedor.builder().compraDireta(compraDireta).fornecedor(f1).build(),
                CompraDiretaFornecedor.builder().compraDireta(compraDireta).fornecedor(f2).build()
        ));

        when(compraDiretaDataProvider.buscarPorId(compraId)).thenReturn(Optional.of(compraDireta));
        when(notificacaoFornecedorDataProvider.existePorCompraDiretaEFornecedor(compraId, 10L)).thenReturn(false);
        when(notificacaoFornecedorDataProvider.existePorCompraDiretaEFornecedor(compraId, 20L)).thenReturn(false);

        useCase.executar(compraId);

        verify(notificacaoFornecedorDataProvider, times(2)).salvar(notificacaoCaptor.capture());
        List<NotificacaoFornecedor> salvas = notificacaoCaptor.getAllValues();

        assertEquals(2, salvas.size());
        assertEquals(f1, salvas.get(0).getFornecedor());
        assertEquals(StatusNotificacao.ENVIADA, salvas.get(0).getStatus());
        assertNotNull(salvas.get(0).getDataEnvio());

        assertEquals(f2, salvas.get(1).getFornecedor());
        assertEquals(StatusNotificacao.ENVIADA, salvas.get(1).getStatus());
        assertNotNull(salvas.get(1).getDataEnvio());
    }

    @Test
    void deveGarantirIdempotenciaNaoDuplicandoNotificacoesJaExistentesTest() {
        Long compraId = 1L;
        Fornecedor f1 = Fornecedor.builder().id(10L).razaoSocial("Fornecedor 1").build();
        Fornecedor f2 = Fornecedor.builder().id(20L).razaoSocial("Fornecedor 2").build();

        CompraDireta compraDireta = CompraDireta.builder().id(compraId).build();
        compraDireta.setFornecedoresConvidados(List.of(
                CompraDiretaFornecedor.builder().compraDireta(compraDireta).fornecedor(f1).build(),
                CompraDiretaFornecedor.builder().compraDireta(compraDireta).fornecedor(f2).build()
        ));

        when(compraDiretaDataProvider.buscarPorId(compraId)).thenReturn(Optional.of(compraDireta));
        when(notificacaoFornecedorDataProvider.existePorCompraDiretaEFornecedor(compraId, 10L)).thenReturn(true);
        when(notificacaoFornecedorDataProvider.existePorCompraDiretaEFornecedor(compraId, 20L)).thenReturn(false);

        useCase.executar(compraId);

        verify(notificacaoFornecedorDataProvider, times(1)).salvar(notificacaoCaptor.capture());
        NotificacaoFornecedor salva = notificacaoCaptor.getValue();
        assertEquals(f2, salva.getFornecedor());
        assertEquals(StatusNotificacao.ENVIADA, salva.getStatus());
    }

    @Test
    void deveLancarRecursoNaoEncontradoExceptionQuandoCompraNaoExisteTest() {
        when(compraDiretaDataProvider.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> useCase.executar(99L));
        verify(notificacaoFornecedorDataProvider, never()).salvar(any());
    }

    @Test
    void deveNaoFazerNadaQuandoCompraNaoPossuiFornecedoresConvidadosTest() {
        Long compraId = 1L;
        CompraDireta compraDireta = CompraDireta.builder().id(compraId).fornecedoresConvidados(Collections.emptyList()).build();

        when(compraDiretaDataProvider.buscarPorId(compraId)).thenReturn(Optional.of(compraDireta));

        useCase.executar(compraId);

        verify(notificacaoFornecedorDataProvider, never()).salvar(any());
    }
}
