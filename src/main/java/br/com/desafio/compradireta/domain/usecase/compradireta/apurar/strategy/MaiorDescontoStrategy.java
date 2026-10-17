package br.com.desafio.compradireta.domain.usecase.compradireta.apurar.strategy;

import java.util.Comparator;
import java.util.List;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;

public class MaiorDescontoStrategy implements IApuracaoVencedorStrategy {

    @Override
    public CriterioJulgamento getCriterio() {
        return CriterioJulgamento.MAIOR_DESCONTO;
    }

    @Override
    public Proposta apurar(List<Proposta> propostas) {
        return propostas.stream()
                .max(Comparator.comparing(Proposta::getPercentualDesconto))
                .orElseThrow();
    }
}
