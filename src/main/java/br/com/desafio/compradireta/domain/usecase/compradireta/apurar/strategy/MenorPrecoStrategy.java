package br.com.desafio.compradireta.domain.usecase.compradireta.apurar.strategy;

import java.util.Comparator;
import java.util.List;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.exception.NegocioException;

public class MenorPrecoStrategy implements IApuracaoVencedorStrategy {

    @Override
    public CriterioJulgamento getCriterio() {
        return CriterioJulgamento.MENOR_PRECO;
    }

    @Override
    public Proposta apurar(List<Proposta> propostas) {
        if (propostas == null || propostas.isEmpty()) {
            throw new NegocioException("Não é possível apurar o vencedor sem propostas válidas");
        }

        return propostas.stream()
                .filter(p -> p.getValorProposto() != null)
                .min(Comparator.comparing(Proposta::getValorProposto))
                .orElseThrow(() -> new NegocioException("Nenhuma proposta válida com valor informado para apuração por menor preço"));
    }
}
