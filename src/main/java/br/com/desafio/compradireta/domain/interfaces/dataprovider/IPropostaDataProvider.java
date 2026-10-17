package br.com.desafio.compradireta.domain.interfaces.dataprovider;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.Proposta;

public interface IPropostaDataProvider {

    Proposta salvar(Proposta proposta);

    List<Proposta> listarPorCompraDireta(Long compraDiretaId);

    Optional<Proposta> buscarPorCompraDiretaEFornecedor(Long compraDiretaId, Long fornecedorId);
}
