package br.com.desafio.compradireta.domain.usecase.fornecedor.listar.converter;

import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.usecase.fornecedor.listar.FornecedorOutput;

public final class FornecedorOutputConverter {

    private FornecedorOutputConverter() {
    }

    public static FornecedorOutput converter(Fornecedor fornecedor) {
        return FornecedorOutput.builder()
                .id(fornecedor.getId())
                .razaoSocial(fornecedor.getRazaoSocial())
                .cpfCnpj(fornecedor.getCpfCnpj())
                .tipoFornecedor(fornecedor.getTipoFornecedor())
                .email(fornecedor.getEmail())
                .telefone(fornecedor.getTelefone())
                .ativo(fornecedor.isAtivo())
                .build();
    }
}
