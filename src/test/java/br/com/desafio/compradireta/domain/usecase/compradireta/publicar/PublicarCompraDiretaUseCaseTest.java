package br.com.desafio.compradireta.domain.usecase.compradireta.publicar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDiretaFornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.exception.NegocioException;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.event.INotificacaoPublicacaoProducer;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;

@ExtendWith(MockitoExtension.class)
class PublicarCompraDiretaUseCaseTest {

    @Mock
    private ICompraDiretaDataProvider compraDiretaDataProvider;

    @Mock
    private INotificacaoPublicacaoProducer notificacaoPublicacaoProducer;

    private PublicarCompraDiretaUseCase publicarCompraDiretaUseCase;

    @BeforeEach
    void setUp() {
        publicarCompraDiretaUseCase = PublicarCompraDiretaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .notificacaoPublicacaoProducer(notificacaoPublicacaoProducer)
                .build();
    }

    @Test
    void devePublicarCompraDiretaEDispararEventoDeNotificacaoTest() {
        Fornecedor fornecedor = Fornecedor.builder().id(1L).razaoSocial("Alpha").email("alpha@teste.com").build();
        CompraDireta compraDireta = compraDiretaEmRascunho(fornecedor);
        when(compraDiretaDataProvider.buscarPorId(10L)).thenReturn(Optional.of(compraDireta));
        when(compraDiretaDataProvider.salvar(any(CompraDireta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompraDiretaResumoOutput output = publicarCompraDiretaUseCase.executar(10L);

        assertEquals(StatusCompraDireta.PUBLICADA, output.getStatus());
        verify(notificacaoPublicacaoProducer).publicar(argThat(event ->
                event.getCompraDiretaId().equals(10L)
                        && "CD-0001/2026".equals(event.getNumero())
                        && event.getFornecedoresConvidadosIds().contains(1L)));
    }

    @Test
    void deveLancarNegocioExceptionQuandoCompraNaoEstaEmRascunhoTest() {
        CompraDireta compraDireta = compraDiretaEmRascunho(Fornecedor.builder().id(1L).build());
        compraDireta.setStatus(StatusCompraDireta.PUBLICADA);
        when(compraDiretaDataProvider.buscarPorId(10L)).thenReturn(Optional.of(compraDireta));

        assertThrows(NegocioException.class, () -> publicarCompraDiretaUseCase.executar(10L));
    }

    @Test
    void deveLancarRecursoNaoEncontradoExceptionQuandoCompraNaoExisteTest() {
        when(compraDiretaDataProvider.buscarPorId(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> publicarCompraDiretaUseCase.executar(10L));
    }

    private CompraDireta compraDiretaEmRascunho(Fornecedor fornecedor) {
        CompraDireta compraDireta = CompraDireta.builder()
                .id(10L)
                .numero("CD-0001/2026")
                .status(StatusCompraDireta.RASCUNHO)
                .build();
        compraDireta.setFornecedoresConvidados(List.of(CompraDiretaFornecedor.builder()
                .compraDireta(compraDireta)
                .fornecedor(fornecedor)
                .build()));
        return compraDireta;
    }
}
