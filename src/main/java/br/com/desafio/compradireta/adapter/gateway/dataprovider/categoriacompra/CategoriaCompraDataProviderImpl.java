package br.com.desafio.compradireta.adapter.gateway.dataprovider.categoriacompra;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.CategoriaCompra;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICategoriaCompraDataProvider;

public class CategoriaCompraDataProviderImpl implements ICategoriaCompraDataProvider {

    private final CategoriaCompraJpaRepository repository;

    public CategoriaCompraDataProviderImpl(CategoriaCompraJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CategoriaCompra> listarTodas() {
        return repository.findAll();
    }

    @Override
    public Optional<CategoriaCompra> buscarPorId(Long id) {
        return repository.findById(id);
    }
}
