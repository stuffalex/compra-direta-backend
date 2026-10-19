package br.com.desafio.compradireta.domain.usecase.proposta.listar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.pagination.Pagina;
import br.com.desafio.compradireta.domain.pagination.Paginacao;
import br.com.desafio.compradireta.domain.usecase.proposta.dto.PropostaOutput;

@ExtendWith(MockitoExtension.class)
class ListarPropostasPorCompraUseCaseTest {

    @Mock
    private IPropostaDataProvider propostaDataProvider;

    private ListarPropostasPorCompraUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = ListarPropostasPorCompraUseCase.builder()
                .propostaDataProvider(propostaDataProvider)
                .build();
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoHouverPropostasTest() {
        Long compraId = 1L;
        Paginacao paginacao = Paginacao.of(0, 10);
        Pagina<Proposta> paginaVazia = Pagina.of(List.of(), 0, 0, 10);

        when(propostaDataProvider.listarPorCompraDiretaPaginado(compraId, paginacao)).thenReturn(paginaVazia);

        Pagina<PropostaOutput> resultado = useCase.executar(compraId, paginacao);

        assertEquals(0, resultado.getContent().size());
        assertEquals(0, resultado.getTotalElements());
        verify(propostaDataProvider).listarPorCompraDiretaPaginado(compraId, paginacao);
    }

    @Test
    void deveListarPropostasComPaginacaoTest() {
        Long compraId = 1L;
        Paginacao paginacao = Paginacao.of(0, 10);
        Fornecedor fornecedor = Fornecedor.builder().id(10L).razaoSocial("Alpha").build();
        CompraDireta compra = CompraDireta.builder().id(compraId).build();
        Proposta proposta = Proposta.builder()
                .id(100L)
                .compraDireta(compra)
                .fornecedor(fornecedor)
                .valorProposto(BigDecimal.valueOf(1500))
                .prazoEntregaDias(5)
                .dataEnvio(LocalDateTime.now())
                .build();

        Pagina<Proposta> pagina = Pagina.of(List.of(proposta), 1, 0, 10);
        when(propostaDataProvider.listarPorCompraDiretaPaginado(compraId, paginacao)).thenReturn(pagina);

        Pagina<PropostaOutput> resultado = useCase.executar(compraId, paginacao);

        assertEquals(1, resultado.getContent().size());
        assertEquals(100L, resultado.getContent().get(0).getId());
        assertEquals(1, resultado.getTotalElements());
        verify(propostaDataProvider).listarPorCompraDiretaPaginado(compraId, paginacao);
    }
}
