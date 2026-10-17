package br.com.desafio.compradireta.domain.interfaces.dataprovider;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.UnidadeCompradora;

public interface IUnidadeCompradoraDataProvider {

    List<UnidadeCompradora> listarTodas();

    Optional<UnidadeCompradora> buscarPorId(Long id);
}
