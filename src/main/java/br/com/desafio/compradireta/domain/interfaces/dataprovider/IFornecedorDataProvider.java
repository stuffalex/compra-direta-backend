package br.com.desafio.compradireta.domain.interfaces.dataprovider;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;

public interface IFornecedorDataProvider {

    List<Fornecedor> listarTodos();

    Optional<Fornecedor> buscarPorId(Long id);

    List<Fornecedor> listarPorIds(List<Long> ids);
}
