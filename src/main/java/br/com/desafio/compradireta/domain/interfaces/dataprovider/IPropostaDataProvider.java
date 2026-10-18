package br.com.desafio.compradireta.domain.interfaces.dataprovider;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.Proposta;
import br.com.desafio.compradireta.domain.pagination.Pagina;
import br.com.desafio.compradireta.domain.pagination.Paginacao;

public interface IPropostaDataProvider {

    Proposta salvar(Proposta proposta);

    List<Proposta> listarPorCompraDireta(Long compraDiretaId);

    Pagina<Proposta> listarPorCompraDiretaPaginado(Long compraDiretaId, Paginacao paginacao);

    Optional<Proposta> buscarPorCompraDiretaEFornecedor(Long compraDiretaId, Long fornecedorId);
}
