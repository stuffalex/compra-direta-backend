package br.com.desafio.compradireta.adapter.gateway.dataprovider.unidadecompradora;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.UnidadeCompradora;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IUnidadeCompradoraDataProvider;

public class UnidadeCompradoraDataProviderImpl implements IUnidadeCompradoraDataProvider {

    private final UnidadeCompradoraJpaRepository repository;

    public UnidadeCompradoraDataProviderImpl(UnidadeCompradoraJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UnidadeCompradora> listarTodas() {
        return repository.findAll();
    }

    @Override
    public Optional<UnidadeCompradora> buscarPorId(Long id) {
        return repository.findById(id);
    }
}
