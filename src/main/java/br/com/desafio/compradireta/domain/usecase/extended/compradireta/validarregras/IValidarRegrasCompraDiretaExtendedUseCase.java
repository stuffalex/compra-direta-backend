package br.com.desafio.compradireta.domain.usecase.extended.compradireta.validarregras;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.Modalidade;

public interface IValidarRegrasCompraDiretaExtendedUseCase {

    void executar(Modalidade modalidade, CriterioJulgamento criterioJulgamento, int quantidadeFornecedoresConvidados);
}
