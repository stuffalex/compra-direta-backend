package br.com.desafio.compradireta.domain.usecase.extended.compradireta.validarregras;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.Modalidade;
import br.com.desafio.compradireta.domain.exception.NegocioException;

class ValidarRegrasCompraDiretaExtendedUseCaseImplTest {

    private ValidarRegrasCompraDiretaExtendedUseCaseImpl validarRegrasCompraDiretaExtendedUseCase;

    @BeforeEach
    void setUp() {
        validarRegrasCompraDiretaExtendedUseCase = ValidarRegrasCompraDiretaExtendedUseCaseImpl.builder().build();
    }

    @Test
    void deveValidarComSucessoQuandoInexigibilidadeComCriterioNaoSeAplicaEUmFornecedorTest() {
        assertDoesNotThrow(() -> validarRegrasCompraDiretaExtendedUseCase.executar(Modalidade.INEXIGIBILIDADE,
                CriterioJulgamento.NAO_SE_APLICA, 1));
    }

    @Test
    void deveLancarNegocioExceptionQuandoInexigibilidadeComCriterioDiferenteDeNaoSeAplicaTest() {
        assertThrows(NegocioException.class, () -> validarRegrasCompraDiretaExtendedUseCase
                .executar(Modalidade.INEXIGIBILIDADE, CriterioJulgamento.MENOR_PRECO, 1));
    }

    @Test
    void deveLancarNegocioExceptionQuandoInexigibilidadeComMaisDeUmFornecedorTest() {
        assertThrows(NegocioException.class, () -> validarRegrasCompraDiretaExtendedUseCase
                .executar(Modalidade.INEXIGIBILIDADE, CriterioJulgamento.NAO_SE_APLICA, 2));
    }

    @Test
    void deveValidarComSucessoQuandoDispensaComCriterioMenorPrecoETresFornecedoresTest() {
        assertDoesNotThrow(() -> validarRegrasCompraDiretaExtendedUseCase.executar(Modalidade.DISPENSA_DE_LICITACAO,
                CriterioJulgamento.MENOR_PRECO, 3));
    }

    @Test
    void deveLancarNegocioExceptionQuandoDispensaComCriterioNaoSeAplicaTest() {
        assertThrows(NegocioException.class, () -> validarRegrasCompraDiretaExtendedUseCase
                .executar(Modalidade.DISPENSA_DE_LICITACAO, CriterioJulgamento.NAO_SE_APLICA, 3));
    }

    @Test
    void deveLancarNegocioExceptionQuandoDispensaComMenosDeTresFornecedoresTest() {
        assertThrows(NegocioException.class, () -> validarRegrasCompraDiretaExtendedUseCase
                .executar(Modalidade.DISPENSA_DE_LICITACAO, CriterioJulgamento.MAIOR_DESCONTO, 2));
    }
}
