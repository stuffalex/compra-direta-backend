package br.com.desafio.compradireta.domain.usecase.compradireta.criar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import br.com.desafio.compradireta.domain.entity.entities.CategoriaCompra;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.UnidadeCompradora;
import br.com.desafio.compradireta.domain.exception.NegocioException;
import br.com.desafio.compradireta.domain.exception.RecursoNaoEncontradoException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICategoriaCompraDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IFornecedorDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IUnidadeCompradoraDataProvider;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.CompraDiretaResumoOutput;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.ItemCompraDiretaInput;
import br.com.desafio.compradireta.domain.usecase.extended.compradireta.validarregras.IValidarRegrasCompraDiretaExtendedUseCase;

@ExtendWith(MockitoExtension.class)
class CriarCompraDiretaUseCaseTest {

    @Mock
    private ICompraDiretaDataProvider compraDiretaDataProvider;
    @Mock
    private IUnidadeCompradoraDataProvider unidadeCompradoraDataProvider;
    @Mock
    private ICategoriaCompraDataProvider categoriaCompraDataProvider;
    @Mock
    private IFornecedorDataProvider fornecedorDataProvider;
    @Mock
    private IValidarRegrasCompraDiretaExtendedUseCase validarRegrasCompraDiretaExtendedUseCase;

    private CriarCompraDiretaUseCase criarCompraDiretaUseCase;

    @BeforeEach
    void setUp() {
        criarCompraDiretaUseCase = CriarCompraDiretaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .unidadeCompradoraDataProvider(unidadeCompradoraDataProvider)
                .categoriaCompraDataProvider(categoriaCompraDataProvider)
                .fornecedorDataProvider(fornecedorDataProvider)
                .validarRegrasCompraDiretaExtendedUseCase(validarRegrasCompraDiretaExtendedUseCase)
                .build();
    }

    @Test
    void deveCriarCompraDiretaComSucessoQuandoDadosValidosTest() {
        CriarCompraDiretaInput input = inputValido();
        List<Fornecedor> fornecedores = List.of(
                Fornecedor.builder().id(1L).build(),
                Fornecedor.builder().id(2L).build(),
                Fornecedor.builder().id(3L).build());

        when(unidadeCompradoraDataProvider.buscarPorId(1L))
                .thenReturn(Optional.of(UnidadeCompradora.builder().id(1L).nome("Secretaria").build()));
        when(categoriaCompraDataProvider.buscarPorId(1L))
                .thenReturn(Optional.of(CategoriaCompra.builder().id(1L).nome("Material").build()));
        when(fornecedorDataProvider.listarPorIds(input.getFornecedoresConvidadosIds())).thenReturn(fornecedores);
        when(compraDiretaDataProvider.contarPorAno(2026)).thenReturn(0L);
        when(compraDiretaDataProvider.salvar(any(CompraDireta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CompraDiretaResumoOutput output = criarCompraDiretaUseCase.executar(input);

        assertEquals("CD-0001/2026", output.getNumero());
        assertEquals(StatusCompraDireta.RASCUNHO, output.getStatus());
        verify(validarRegrasCompraDiretaExtendedUseCase).executar(Modalidade.DISPENSA_DE_LICITACAO,
                CriterioJulgamento.MENOR_PRECO, 3);
        verify(compraDiretaDataProvider).salvar(any(CompraDireta.class));
    }

    @Test
    void deveLancarNegocioExceptionQuandoCompraDiretaSemItensTest() {
        CriarCompraDiretaInput input = inputValido();
        input.setItens(List.of());

        assertThrows(NegocioException.class, () -> criarCompraDiretaUseCase.executar(input));
    }

    @Test
    void deveLancarRecursoNaoEncontradoExceptionQuandoUnidadeCompradoraNaoExisteTest() {
        CriarCompraDiretaInput input = inputValido();
        when(unidadeCompradoraDataProvider.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> criarCompraDiretaUseCase.executar(input));
    }

    @Test
    void deveLancarNegocioExceptionQuandoFornecedorConvidadoNaoExisteTest() {
        CriarCompraDiretaInput input = inputValido();
        when(unidadeCompradoraDataProvider.buscarPorId(1L))
                .thenReturn(Optional.of(UnidadeCompradora.builder().id(1L).build()));
        when(categoriaCompraDataProvider.buscarPorId(1L))
                .thenReturn(Optional.of(CategoriaCompra.builder().id(1L).build()));
        when(fornecedorDataProvider.listarPorIds(input.getFornecedoresConvidadosIds()))
                .thenReturn(List.of(Fornecedor.builder().id(1L).build()));

        assertThrows(NegocioException.class, () -> criarCompraDiretaUseCase.executar(input));
    }

    private CriarCompraDiretaInput inputValido() {
        CriarCompraDiretaInput input = new CriarCompraDiretaInput();
        input.setObjeto("Aquisição de material de escritório");
        input.setJustificativa("Reposição de estoque");
        input.setModalidade(Modalidade.DISPENSA_DE_LICITACAO);
        input.setCriterioJulgamento(CriterioJulgamento.MENOR_PRECO);
        input.setUnidadeCompradoraId(1L);
        input.setCategoriaCompraId(1L);
        input.setDataAbertura(LocalDate.of(2026, 10, 10));
        input.setDataLimitePropostas(LocalDate.of(2026, 10, 25));
        ItemCompraDiretaInput item = new ItemCompraDiretaInput();
        item.setDescricao("Papel A4");
        item.setUnidadeMedida("RESMA");
        item.setQuantidade(new BigDecimal("100"));
        item.setValorUnitarioEstimado(new BigDecimal("24.90"));
        input.setItens(List.of(item));
        input.setFornecedoresConvidadosIds(List.of(1L, 2L, 3L));
        return input;
    }
}
