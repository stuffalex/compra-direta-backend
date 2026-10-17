package br.com.desafio.compradireta.domain.usecase.categoriacompra.listar.converter;

import br.com.desafio.compradireta.domain.entity.entities.CategoriaCompra;
import br.com.desafio.compradireta.domain.usecase.categoriacompra.listar.CategoriaCompraOutput;

public final class CategoriaCompraOutputConverter {

    private CategoriaCompraOutputConverter() {
    }

    public static CategoriaCompraOutput converter(CategoriaCompra categoriaCompra) {
        return CategoriaCompraOutput.builder()
                .id(categoriaCompra.getId())
                .nome(categoriaCompra.getNome())
                .build();
    }
}
