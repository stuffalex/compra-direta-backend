package br.com.desafio.compradireta.domain.usecase.categoriacompra.listar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoriaCompraOutput {

    private Long id;
    private String nome;
}
