package br.com.desafio.compradireta.domain.usecase.notificacao.listar.converter;

import br.com.desafio.compradireta.domain.entity.entities.NotificacaoFornecedor;
import br.com.desafio.compradireta.domain.usecase.notificacao.listar.NotificacaoFornecedorOutput;

public final class NotificacaoFornecedorOutputConverter {

    private NotificacaoFornecedorOutputConverter() {
    }

    public static NotificacaoFornecedorOutput converter(NotificacaoFornecedor notificacaoFornecedor) {
        return NotificacaoFornecedorOutput.builder()
                .fornecedorId(notificacaoFornecedor.getFornecedor().getId())
                .fornecedorNome(notificacaoFornecedor.getFornecedor().getRazaoSocial())
                .dataEnvio(notificacaoFornecedor.getDataEnvio())
                .status(notificacaoFornecedor.getStatus())
                .build();
    }
}
