package br.com.desafio.compradireta.domain.usecase.compradireta.apurar;

import java.util.Comparator;
import java.util.List;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.exception.NegocioException;
import br.com.desafio.compradireta.domain.validation.Validator;

public class MaiorDescontoStrategy implements IApuracaoVencedorStrategy {

    @Override
    public CriterioJulgamento getCriterio() {
        return CriterioJulgamento.MAIOR_DESCONTO;
    }

    @Override
    public Proposta apurar(List<Proposta> propostas) {
        Validator.isTrue(propostas == null || propostas.isEmpty(), "Não é possível apurar o vencedor sem propostas válidas");

        return propostas.stream()
                .filter(p -> p.getPercentualDesconto() != null)
                .max(Comparator.comparing(Proposta::getPercentualDesconto))
                .orElseThrow(() -> new NegocioException("Nenhuma proposta válida com desconto informado para apuração por maior desconto"));
    }
}