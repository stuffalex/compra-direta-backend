package br.com.desafio.compradireta.domain.usecase.compradireta.apurar;

import java.util.List;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.validation.Validator;

public class InexigibilidadeStrategy implements IApuracaoVencedorStrategy {

    @Override
    public CriterioJulgamento getCriterio() {
        return CriterioJulgamento.NAO_SE_APLICA;
    }

    @Override
    public Proposta apurar(List<Proposta> propostas) {
        Validator.isTrue(propostas != null && propostas.size() == 1,
                "Para compras com critério NÃO SE APLICA (inexigibilidade), deve haver exatamente 1 proposta registrada");

        return propostas.get(0);
    }
}
