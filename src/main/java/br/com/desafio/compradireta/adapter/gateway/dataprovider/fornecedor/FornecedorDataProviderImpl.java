package br.com.desafio.compradireta.adapter.gateway.dataprovider.fornecedor;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IFornecedorDataProvider;

public class FornecedorDataProviderImpl implements IFornecedorDataProvider {

    private final FornecedorJpaRepository repository;

    public FornecedorDataProviderImpl(FornecedorJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Fornecedor> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Optional<Fornecedor> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Fornecedor> listarPorIds(List<Long> ids) {
        return repository.findByIdIn(ids);
    }
}
