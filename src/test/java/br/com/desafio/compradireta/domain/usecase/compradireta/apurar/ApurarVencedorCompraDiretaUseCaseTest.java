package br.com.desafio.compradireta.domain.usecase.compradireta.apurar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.Modalidade;
import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.exception.NegocioException;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;

@ExtendWith(MockitoExtension.class)
class ApurarVencedorCompraDiretaUseCaseTest {

    @Mock
    private ICompraDiretaDataProvider compraDiretaDataProvider;

    @Mock
    private IPropostaDataProvider propostaDataProvider;

    private ApurarVencedorCompraDiretaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ApurarVencedorCompraDiretaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .propostaDataProvider(propostaDataProvider)
                .strategies(List.of(
                        new MenorPrecoStrategy(),
                        new MaiorDescontoStrategy(),
                        new InexigibilidadeStrategy()
                ))
                .build();
    }

    @Test
    void deveApurarVencedorComSucessoQuandoCriterioMenorPrecoTest() {
        Long compraId = 1L;
        CompraDireta compra = criarCompra(compraId, StatusCompraDireta.PUBLICADA, CriterioJulgamento.MENOR_PRECO);

        Fornecedor f1 = Fornecedor.builder().id(10L).razaoSocial("Fornecedor A").build();
        Fornecedor f2 = Fornecedor.builder().id(20L).razaoSocial("Fornecedor B").build();

        Proposta p1 = Proposta.builder().id(101L).fornecedor(f1).valorProposto(new BigDecimal("200.00")).build();
        Proposta p2 = Proposta.builder().id(102L).fornecedor(f2).valorProposto(new BigDecimal("150.00")).build();

        when(compraDiretaDataProvider.buscarPorId(compraId)).thenReturn(Optional.of(compra));
        when(propostaDataProvider.listarPorCompraDireta(compraId)).thenReturn(List.of(p1, p2));
        when(compraDiretaDataProvider.salvar(any(CompraDireta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompraDiretaResumoOutput output = useCase.executar(compraId);

        assertEquals(StatusCompraDireta.HOMOLOGADA, output.getStatus());
        assertEquals(f2, compra.getFornecedorVencedor());
        assertNotNull(compra.getDataHomologacao());
        verify(compraDiretaDataProvider).salvar(compra);
    }

    @Test
    void deveApurarVencedorComSucessoQuandoCriterioMaiorDescontoTest() {
        Long compraId = 2L;
        CompraDireta compra = criarCompra(compraId, StatusCompraDireta.PUBLICADA, CriterioJulgamento.MAIOR_DESCONTO);

        Fornecedor f1 = Fornecedor.builder().id(10L).razaoSocial("Fornecedor A").build();
        Fornecedor f2 = Fornecedor.builder().id(20L).razaoSocial("Fornecedor B").build();

        Proposta p1 = Proposta.builder().id(101L).fornecedor(f1).percentualDesconto(new BigDecimal("10.00")).build();
        Proposta p2 = Proposta.builder().id(102L).fornecedor(f2).percentualDesconto(new BigDecimal("25.00")).build();

        when(compraDiretaDataProvider.buscarPorId(compraId)).thenReturn(Optional.of(compra));
        when(propostaDataProvider.listarPorCompraDireta(compraId)).thenReturn(List.of(p1, p2));
        when(compraDiretaDataProvider.salvar(any(CompraDireta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompraDiretaResumoOutput output = useCase.executar(compraId);

        assertEquals(StatusCompraDireta.HOMOLOGADA, output.getStatus());
        assertEquals(f2, compra.getFornecedorVencedor());
        assertNotNull(compra.getDataHomologacao());
        verify(compraDiretaDataProvider).salvar(compra);
    }

    @Test
    void deveApurarVencedorComSucessoQuandoCriterioNaoSeAplicaTest() {
        Long compraId = 3L;
        CompraDireta compra = criarCompra(compraId, StatusCompraDireta.PUBLICADA, CriterioJulgamento.NAO_SE_APLICA);
        compra.setModalidade(Modalidade.INEXIGIBILIDADE);

        Fornecedor f1 = Fornecedor.builder().id(10L).razaoSocial("Fornecedor Exclusivo").build();
        Proposta p1 = Proposta.builder().id(101L).fornecedor(f1).prazoEntregaDias(5).build();

        when(compraDiretaDataProvider.buscarPorId(compraId)).thenReturn(Optional.of(compra));
        when(propostaDataProvider.listarPorCompraDireta(compraId)).thenReturn(List.of(p1));
        when(compraDiretaDataProvider.salvar(any(CompraDireta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompraDiretaResumoOutput output = useCase.executar(compraId);

        assertEquals(StatusCompraDireta.HOMOLOGADA, output.getStatus());
        assertEquals(f1, compra.getFornecedorVencedor());
        assertNotNull(compra.getDataHomologacao());
        verify(compraDiretaDataProvider).salvar(compra);
    }

    @Test
    void deveLancarRecursoNaoEncontradoExceptionQuandoCompraNaoExisteTest() {
        when(compraDiretaDataProvider.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> useCase.executar(99L));
    }

    @Test
    void deveLancarNegocioExceptionQuandoCompraNaoEstaPublicadaOuEmJulgamentoTest() {
        CompraDireta compra = criarCompra(1L, StatusCompraDireta.RASCUNHO, CriterioJulgamento.MENOR_PRECO);
        when(compraDiretaDataProvider.buscarPorId(1L)).thenReturn(Optional.of(compra));

        assertThrows(NegocioException.class, () -> useCase.executar(1L));
    }

    @Test
    void deveLancarNegocioExceptionQuandoListaDePropostasVaziaTest() {
        CompraDireta compra = criarCompra(1L, StatusCompraDireta.PUBLICADA, CriterioJulgamento.MENOR_PRECO);
        when(compraDiretaDataProvider.buscarPorId(1L)).thenReturn(Optional.of(compra));
        when(propostaDataProvider.listarPorCompraDireta(1L)).thenReturn(List.of());

        assertThrows(NegocioException.class, () -> useCase.executar(1L));
    }

    @Test
    void deveLancarNegocioExceptionQuandoEstrategiaNaoEncontradaParaCriterioTest() {
        ApurarVencedorCompraDiretaUseCase useCaseSemEstrategias = ApurarVencedorCompraDiretaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .propostaDataProvider(propostaDataProvider)
                .strategies(List.of())
                .build();

        CompraDireta compra = criarCompra(1L, StatusCompraDireta.PUBLICADA, CriterioJulgamento.MENOR_PRECO);
        Proposta p1 = Proposta.builder().id(1L).build();

        when(compraDiretaDataProvider.buscarPorId(1L)).thenReturn(Optional.of(compra));
        when(propostaDataProvider.listarPorCompraDireta(1L)).thenReturn(List.of(p1));

        assertThrows(NegocioException.class, () -> useCaseSemEstrategias.executar(1L));
    }

    private CompraDireta criarCompra(Long id, StatusCompraDireta status, CriterioJulgamento criterio) {
        return CompraDireta.builder()
                .id(id)
                .numero("CD-0001/2026")
                .status(status)
                .criterioJulgamento(criterio)
                .modalidade(Modalidade.DISPENSA_DE_LICITACAO)
                .itens(List.of())
                .build();
    }
}
