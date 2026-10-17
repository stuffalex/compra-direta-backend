package br.com.desafio.compradireta.domain.usecase.compradireta.apurar.strategy;

import java.util.Comparator;
import java.util.List;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;

public class MenorPrecoStrategy implements IApuracaoVencedorStrategy {

    @Override
    public CriterioJulgamento getCriterio() {
        return CriterioJulgamento.MENOR_PRECO;
    }

    @Override
    public Proposta apurar(List<Proposta> propostas) {
        return propostas.stream()
                .min(Comparator.comparing(Proposta::getValorProposto))
                .orElseThrow();
    }
}
