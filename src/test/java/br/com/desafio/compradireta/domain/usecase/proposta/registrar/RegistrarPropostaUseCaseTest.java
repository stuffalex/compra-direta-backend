package br.com.desafio.compradireta.domain.usecase.proposta.registrar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.CompraDiretaFornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.exception.NegocioException;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IFornecedorDataProvider;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.usecase.proposta.dto.PropostaOutput;

@ExtendWith(MockitoExtension.class)
class RegistrarPropostaUseCaseTest {

    @Mock
    private ICompraDiretaDataProvider compraDiretaDataProvider;
    @Mock
    private IFornecedorDataProvider fornecedorDataProvider;
    @Mock
    private IPropostaDataProvider propostaDataProvider;

    private RegistrarPropostaUseCase registrarPropostaUseCase;

    @BeforeEach
    void setUp() {
        registrarPropostaUseCase = RegistrarPropostaUseCase.builder()
                .compraDiretaDataProvider(compraDiretaDataProvider)
                .fornecedorDataProvider(fornecedorDataProvider)
                .propostaDataProvider(propostaDataProvider)
                .build();
    }

    @Test
    void deveRegistrarPropostaComSucessoQuandoCompraPublicadaEFornecedorConvidadoTest() {
        Fornecedor fornecedor = Fornecedor.builder().id(1L).razaoSocial("Alpha").build();
        CompraDireta compraDireta = compraDiretaPublicadaComConvidado(fornecedor, CriterioJulgamento.MENOR_PRECO);
        RegistrarPropostaInput input = new RegistrarPropostaInput(10L, 1L, new BigDecimal("100.00"), null, 5);

        when(compraDiretaDataProvider.buscarPorId(10L)).thenReturn(Optional.of(compraDireta));
        when(fornecedorDataProvider.buscarPorId(1L)).thenReturn(Optional.of(fornecedor));
        when(propostaDataProvider.buscarPorCompraDiretaEFornecedor(10L, 1L)).thenReturn(Optional.empty());
        when(propostaDataProvider.salvar(any(Proposta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PropostaOutput output = registrarPropostaUseCase.executar(input);

        assertEquals(1L, output.getFornecedorId());
        assertEquals(new BigDecimal("100.00"), output.getValorProposto());
    }

    @Test
    void deveLancarNegocioExceptionQuandoCompraNaoEstaPublicadaTest() {
        Fornecedor fornecedor = Fornecedor.builder().id(1L).build();
        CompraDireta compraDireta = compraDiretaPublicadaComConvidado(fornecedor, CriterioJulgamento.MENOR_PRECO);
        compraDireta.setStatus(StatusCompraDireta.RASCUNHO);
        RegistrarPropostaInput input = new RegistrarPropostaInput(10L, 1L, new BigDecimal("100.00"), null, 5);

        when(compraDiretaDataProvider.buscarPorId(10L)).thenReturn(Optional.of(compraDireta));

        assertThrows(NegocioException.class, () -> registrarPropostaUseCase.executar(input));
    }

    @Test
    void deveLancarNegocioExceptionQuandoFornecedorNaoFoiConvidadoTest() {
        Fornecedor convidado = Fornecedor.builder().id(1L).build();
        Fornecedor naoConvidado = Fornecedor.builder().id(99L).build();
        CompraDireta compraDireta = compraDiretaPublicadaComConvidado(convidado, CriterioJulgamento.MENOR_PRECO);
        RegistrarPropostaInput input = new RegistrarPropostaInput(10L, 99L, new BigDecimal("100.00"), null, 5);

        when(compraDiretaDataProvider.buscarPorId(10L)).thenReturn(Optional.of(compraDireta));
        when(fornecedorDataProvider.buscarPorId(99L)).thenReturn(Optional.of(naoConvidado));

        assertThrows(NegocioException.class, () -> registrarPropostaUseCase.executar(input));
    }

    @Test
    void deveLancarNegocioExceptionQuandoFornecedorJaEnviouPropostaTest() {
        Fornecedor fornecedor = Fornecedor.builder().id(1L).build();
        CompraDireta compraDireta = compraDiretaPublicadaComConvidado(fornecedor, CriterioJulgamento.MENOR_PRECO);
        RegistrarPropostaInput input = new RegistrarPropostaInput(10L, 1L, new BigDecimal("100.00"), null, 5);

        when(compraDiretaDataProvider.buscarPorId(10L)).thenReturn(Optional.of(compraDireta));
        when(fornecedorDataProvider.buscarPorId(1L)).thenReturn(Optional.of(fornecedor));
        when(propostaDataProvider.buscarPorCompraDiretaEFornecedor(10L, 1L))
                .thenReturn(Optional.of(Proposta.builder().build()));

        assertThrows(NegocioException.class, () -> registrarPropostaUseCase.executar(input));
    }

    @Test
    void deveLancarNegocioExceptionQuandoValorPropostoAusenteParaCriterioMenorPrecoTest() {
        Fornecedor fornecedor = Fornecedor.builder().id(1L).build();
        CompraDireta compraDireta = compraDiretaPublicadaComConvidado(fornecedor, CriterioJulgamento.MENOR_PRECO);
        RegistrarPropostaInput input = new RegistrarPropostaInput(10L, 1L, null, null, 5);

        when(compraDiretaDataProvider.buscarPorId(10L)).thenReturn(Optional.of(compraDireta));
        when(fornecedorDataProvider.buscarPorId(1L)).thenReturn(Optional.of(fornecedor));
        when(propostaDataProvider.buscarPorCompraDiretaEFornecedor(10L, 1L)).thenReturn(Optional.empty());

        assertThrows(NegocioException.class, () -> registrarPropostaUseCase.executar(input));
    }

    private CompraDireta compraDiretaPublicadaComConvidado(Fornecedor fornecedor, CriterioJulgamento criterioJulgamento) {
        CompraDireta compraDireta = CompraDireta.builder()
                .id(10L)
                .status(StatusCompraDireta.PUBLICADA)
                .criterioJulgamento(criterioJulgamento)
                .dataLimitePropostas(LocalDate.of(2030, 1, 1))
                .build();
        compraDireta.setFornecedoresConvidados(List.of(CompraDiretaFornecedor.builder()
                .compraDireta(compraDireta)
                .fornecedor(fornecedor)
                .build()));
        return compraDireta;
    }
}
