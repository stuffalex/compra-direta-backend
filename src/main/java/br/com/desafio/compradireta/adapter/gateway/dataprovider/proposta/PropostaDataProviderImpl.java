package br.com.desafio.compradireta.adapter.gateway.dataprovider.proposta;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;

public class PropostaDataProviderImpl implements IPropostaDataProvider {

    private final PropostaJpaRepository repository;

    public PropostaDataProviderImpl(PropostaJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Proposta salvar(Proposta proposta) {
        return repository.save(proposta);
    }

    @Override
    public List<Proposta> listarPorCompraDireta(Long compraDiretaId) {
        return repository.findByCompraDiretaId(compraDiretaId);
    }

    @Override
    public Optional<Proposta> buscarPorCompraDiretaEFornecedor(Long compraDiretaId, Long fornecedorId) {
        return repository.findByCompraDiretaIdAndFornecedorId(compraDiretaId, fornecedorId);
    }
}
