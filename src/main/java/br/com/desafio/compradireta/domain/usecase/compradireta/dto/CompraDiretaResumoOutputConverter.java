package br.com.desafio.compradireta.domain.usecase.compradireta.dto;

import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;

public final class CompraDiretaResumoOutputConverter {

    private CompraDiretaResumoOutputConverter() {
    }

    public static CompraDiretaResumoOutput converter(CompraDireta compraDireta) {
        return CompraDiretaResumoOutput.builder()
                .id(compraDireta.getId())
                .numero(compraDireta.getNumero())
                .objeto(compraDireta.getObjeto())
                .modalidade(compraDireta.getModalidade())
                .criterioJulgamento(compraDireta.getCriterioJulgamento())
                .status(compraDireta.getStatus())
                .valorEstimadoTotal(compraDireta.getValorEstimadoTotal())
                .dataAbertura(compraDireta.getDataAbertura())
                .dataLimitePropostas(compraDireta.getDataLimitePropostas())
                .build();
    }
}
