package br.com.desafio.compradireta.domain.usecase.compradireta.apurar;

import java.util.Comparator;
import java.util.List;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.exception.NegocioException;
import br.com.desafio.compradireta.domain.validation.Validator;

public class MenorPrecoStrategy implements IApuracaoVencedorStrategy {

    @Override
    public CriterioJulgamento getCriterio() {
        return CriterioJulgamento.MENOR_PRECO;
    }

    @Override
    public Proposta apurar(List<Proposta> propostas) {
        Validator.isTrue(propostas == null || propostas.isEmpty(), "Não é possível apurar o vencedor sem propostas válidas");

        return propostas.stream()
                .filter(p -> p.getValorProposto() != null)
                .min(Comparator.comparing(Proposta::getValorProposto))
                .orElseThrow(() -> new NegocioException("Nenhuma proposta válida com valor informado para apuração por menor preço"));
    }
}
