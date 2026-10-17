package br.com.desafio.compradireta.adapter.gateway.dataprovider.notificacao;

import java.util.List;

import br.com.desafio.compradireta.domain.entity.entities.NotificacaoFornecedor;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.INotificacaoFornecedorDataProvider;

public class NotificacaoFornecedorDataProviderImpl implements INotificacaoFornecedorDataProvider {

    private final NotificacaoFornecedorJpaRepository repository;

    public NotificacaoFornecedorDataProviderImpl(NotificacaoFornecedorJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public NotificacaoFornecedor salvar(NotificacaoFornecedor notificacaoFornecedor) {
        return repository.save(notificacaoFornecedor);
    }

    @Override
    public boolean existePorCompraDiretaEFornecedor(Long compraDiretaId, Long fornecedorId) {
        return repository.existsByCompraDiretaIdAndFornecedorId(compraDiretaId, fornecedorId);
    }

    @Override
    public List<NotificacaoFornecedor> listarPorCompraDireta(Long compraDiretaId) {
        return repository.findByCompraDiretaId(compraDiretaId);
    }
}
