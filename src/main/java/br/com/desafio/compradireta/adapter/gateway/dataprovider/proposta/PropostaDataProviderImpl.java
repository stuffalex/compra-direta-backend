package br.com.desafio.compradireta.adapter.gateway.dataprovider.proposta;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.interfaces.dataprovider.IPropostaDataProvider;
import br.com.desafio.compradireta.domain.pagination.Pagina;
import br.com.desafio.compradireta.domain.pagination.Paginacao;

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
    public Pagina<Proposta> listarPorCompraDiretaPaginado(Long compraDiretaId, Paginacao paginacao) {
        PageRequest pageRequest = PageRequest.of(paginacao.getPagina(), paginacao.getTamanho());
        Page<Proposta> page = repository.findByCompraDiretaId(compraDiretaId, pageRequest);
        return Pagina.of(page.getContent(), page.getTotalElements(), page.getNumber(), page.getSize());
    }

    @Override
    public Optional<Proposta> buscarPorCompraDiretaEFornecedor(Long compraDiretaId, Long fornecedorId) {
        return repository.findByCompraDiretaIdAndFornecedorId(compraDiretaId, fornecedorId);
    }
}
