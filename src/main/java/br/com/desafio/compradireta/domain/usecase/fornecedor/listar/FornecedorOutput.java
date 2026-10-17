package br.com.desafio.compradireta.domain.usecase.fornecedor.listar;

import br.com.desafio.compradireta.domain.constant.TipoFornecedor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FornecedorOutput {

    private Long id;
    private String razaoSocial;
    private String cpfCnpj;
    private TipoFornecedor tipoFornecedor;
    private String email;
    private String telefone;
    private boolean ativo;
}
