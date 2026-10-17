package br.com.desafio.compradireta.domain.usecase.compradireta.apurar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.exception.NegocioException;

class MaiorDescontoStrategyTest {

    private MaiorDescontoStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MaiorDescontoStrategy();
    }

    @Test
    void deveRetornarCriterioMaiorDescontoTest() {
        assertEquals(CriterioJulgamento.MAIOR_DESCONTO, strategy.getCriterio());
    }

    @Test
    void deveApurarPropostaComMaiorDescontoComSucessoTest() {
        Fornecedor fornecedor1 = Fornecedor.builder().id(1L).razaoSocial("Fornecedor 1").build();
        Fornecedor fornecedor2 = Fornecedor.builder().id(2L).razaoSocial("Fornecedor 2").build();
        Fornecedor fornecedor3 = Fornecedor.builder().id(3L).razaoSocial("Fornecedor 3").build();

        Proposta p1 = Proposta.builder().id(101L).fornecedor(fornecedor1).percentualDesconto(new BigDecimal("5.50")).build();
        Proposta p2 = Proposta.builder().id(102L).fornecedor(fornecedor2).percentualDesconto(new BigDecimal("18.00")).build();
        Proposta p3 = Proposta.builder().id(103L).fornecedor(fornecedor3).percentualDesconto(new BigDecimal("12.25")).build();

        Proposta vencedora = strategy.apurar(List.of(p1, p2, p3));

        assertEquals(102L, vencedora.getId());
        assertEquals(new BigDecimal("18.00"), vencedora.getPercentualDesconto());
        assertEquals("Fornecedor 2", vencedora.getFornecedor().getRazaoSocial());
    }

    @Test
    void deveLancarNegocioExceptionQuandoListaDePropostasEstiverVaziaOuNulaTest() {
        assertThrows(NegocioException.class, () -> strategy.apurar(List.of()));
        assertThrows(NegocioException.class, () -> strategy.apurar(null));
    }

    @Test
    void deveLancarNegocioExceptionQuandoNenhumaPropostaTiverDescontoInformadoTest() {
        Proposta p1 = Proposta.builder().id(1L).percentualDesconto(null).build();
        assertThrows(NegocioException.class, () -> strategy.apurar(List.of(p1)));
    }
}
