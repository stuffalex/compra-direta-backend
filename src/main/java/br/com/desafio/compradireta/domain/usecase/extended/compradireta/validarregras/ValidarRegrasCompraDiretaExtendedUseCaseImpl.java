package br.com.desafio.compradireta.domain.usecase.extended.compradireta.validarregras;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.Modalidade;
import br.com.desafio.compradireta.domain.validation.Validator;
import lombok.Builder;

@Builder
public class ValidarRegrasCompraDiretaExtendedUseCaseImpl implements IValidarRegrasCompraDiretaExtendedUseCase {

    private static final int MINIMO_FORNECEDORES_DISPENSA = 3;
    private static final int FORNECEDORES_INEXIGIBILIDADE = 1;

    @Override
    public void executar(Modalidade modalidade, CriterioJulgamento criterioJulgamento, int quantidadeFornecedoresConvidados) {
        if (modalidade == Modalidade.INEXIGIBILIDADE) {
            Validator.isTrue(criterioJulgamento == CriterioJulgamento.NAO_SE_APLICA,
                    "Para a modalidade INEXIGIBILIDADE o critério de julgamento deve ser NAO_SE_APLICA");
            Validator.isTrue(quantidadeFornecedoresConvidados == FORNECEDORES_INEXIGIBILIDADE,
                    "Para a modalidade INEXIGIBILIDADE a compra deve ter exatamente 1 fornecedor convidado (fonte exclusiva)");
            return;
        }

        Validator.isTrue(criterioJulgamento == CriterioJulgamento.MENOR_PRECO
                        || criterioJulgamento == CriterioJulgamento.MAIOR_DESCONTO,
                "Para a modalidade DISPENSA_DE_LICITACAO o critério de julgamento deve ser MENOR_PRECO ou MAIOR_DESCONTO");
        Validator.isTrue(quantidadeFornecedoresConvidados >= MINIMO_FORNECEDORES_DISPENSA,
                "Para a modalidade DISPENSA_DE_LICITACAO a compra deve ter no mínimo " + MINIMO_FORNECEDORES_DISPENSA
                        + " fornecedores convidados");
    }
}
