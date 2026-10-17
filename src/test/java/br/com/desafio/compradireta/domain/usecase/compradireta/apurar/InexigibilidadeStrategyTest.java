package br.com.desafio.compradireta.domain.usecase.compradireta.apurar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.exception.NegocioException;

class InexigibilidadeStrategyTest {

    private InexigibilidadeStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new InexigibilidadeStrategy();
    }

    @Test
    void deveRetornarCriterioNaoSeAplicaTest() {
        assertEquals(CriterioJulgamento.NAO_SE_APLICA, strategy.getCriterio());
    }

    @Test
    void deveApurarPropostaUnicaComSucessoParaInexigibilidadeTest() {
        Fornecedor fornecedor = Fornecedor.builder().id(1L).razaoSocial("Fornecedor Exclusivo").build();
        Proposta proposta = Proposta.builder().id(101L).fornecedor(fornecedor).prazoEntregaDias(10).build();

        Proposta vencedora = strategy.apurar(List.of(proposta));

        assertEquals(101L, vencedora.getId());
        assertEquals("Fornecedor Exclusivo", vencedora.getFornecedor().getRazaoSocial());
    }

    @Test
    void deveLancarNegocioExceptionQuandoListaNulaOuVaziaTest() {
        assertThrows(NegocioException.class, () -> strategy.apurar(null));
        assertThrows(NegocioException.class, () -> strategy.apurar(List.of()));
    }

    @Test
    void deveLancarNegocioExceptionQuandoHouverMaisDeUmaPropostaTest() {
        Proposta p1 = Proposta.builder().id(1L).build();
        Proposta p2 = Proposta.builder().id(2L).build();

        assertThrows(NegocioException.class, () -> strategy.apurar(List.of(p1, p2)));
    }
}
