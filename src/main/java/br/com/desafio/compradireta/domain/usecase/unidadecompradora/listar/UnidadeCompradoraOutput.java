package br.com.desafio.compradireta.domain.usecase.unidadecompradora.listar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UnidadeCompradoraOutput {

    private Long id;
    private String nome;
    private String codigo;
}
