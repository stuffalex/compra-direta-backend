package br.com.desafio.compradireta.domain.usecase.unidadecompradora.listar.converter;

import br.com.desafio.compradireta.domain.entity.entities.UnidadeCompradora;
import br.com.desafio.compradireta.domain.usecase.unidadecompradora.listar.UnidadeCompradoraOutput;

public final class UnidadeCompradoraOutputConverter {

    private UnidadeCompradoraOutputConverter() {
    }

    public static UnidadeCompradoraOutput converter(UnidadeCompradora unidadeCompradora) {
        return UnidadeCompradoraOutput.builder()
                .id(unidadeCompradora.getId())
                .nome(unidadeCompradora.getNome())
                .codigo(unidadeCompradora.getCodigo())
                .build();
    }
}
