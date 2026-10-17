package br.com.desafio.compradireta.adapter.gateway.dataprovider.compradireta;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;

public class CompraDiretaDataProviderImpl implements ICompraDiretaDataProvider {

    private final CompraDiretaJpaRepository repository;

    public CompraDiretaDataProviderImpl(CompraDiretaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompraDireta salvar(CompraDireta compraDireta) {
        return repository.save(compraDireta);
    }

    @Override
    public Optional<CompraDireta> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<CompraDireta> listarTodas() {
        return repository.findAll();
    }

    @Override
    public long contarPorAno(int ano) {
        return repository.contarPorAno(ano);
    }
}
