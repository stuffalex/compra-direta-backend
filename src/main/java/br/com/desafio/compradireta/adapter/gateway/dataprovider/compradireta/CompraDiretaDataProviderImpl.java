package br.com.desafio.compradireta.adapter.gateway.dataprovider.compradireta;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.ICompraDiretaDataProvider;
import br.com.desafio.compradireta.domain.pagination.Pagina;
import br.com.desafio.compradireta.domain.pagination.Paginacao;

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
    public Pagina<CompraDireta> listarPaginado(Paginacao paginacao) {
        PageRequest pageRequest = PageRequest.of(paginacao.getPagina(), paginacao.getTamanho());
        Page<CompraDireta> page = repository.findAll(pageRequest);
        return Pagina.of(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
    }

    @Override
    public long contarPorAno(int ano) {
        return repository.contarPorAno(ano);
    }
}
