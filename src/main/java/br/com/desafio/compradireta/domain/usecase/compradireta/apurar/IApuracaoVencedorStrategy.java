package br.com.desafio.compradireta.domain.usecase.compradireta.apurar;

import java.util.List;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.entity.entities.Proposta;

public interface IApuracaoVencedorStrategy {

    CriterioJulgamento getCriterio();

    Proposta apurar(List<Proposta> propostas);
}
