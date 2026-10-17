package br.com.desafio.compradireta.domain.interfaces.dataprovider;

import java.util.List;

import br.com.desafio.compradireta.domain.entity.entities.NotificacaoFornecedor;

public interface INotificacaoFornecedorDataProvider {

    NotificacaoFornecedor salvar(NotificacaoFornecedor notificacaoFornecedor);

    boolean existePorCompraDiretaEFornecedor(Long compraDiretaId, Long fornecedorId);

    List<NotificacaoFornecedor> listarPorCompraDireta(Long compraDiretaId);
}
