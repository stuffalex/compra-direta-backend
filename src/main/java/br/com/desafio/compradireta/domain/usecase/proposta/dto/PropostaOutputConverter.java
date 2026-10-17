package br.com.desafio.compradireta.domain.usecase.proposta.dto;

import br.com.desafio.compradireta.domain.entity.entities.Proposta;

public final class PropostaOutputConverter {

    private PropostaOutputConverter() {
    }

    public static PropostaOutput converter(Proposta proposta) {
        return PropostaOutput.builder()
                .id(proposta.getId())
                .fornecedorId(proposta.getFornecedor().getId())
                .fornecedorNome(proposta.getFornecedor().getRazaoSocial())
                .valorProposto(proposta.getValorProposto())
                .percentualDesconto(proposta.getPercentualDesconto())
                .prazoEntregaDias(proposta.getPrazoEntregaDias())
                .dataEnvio(proposta.getDataEnvio())
                .build();
    }
}
