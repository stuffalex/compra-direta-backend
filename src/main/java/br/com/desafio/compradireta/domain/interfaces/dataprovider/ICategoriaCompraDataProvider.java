package br.com.desafio.compradireta.domain.interfaces.dataprovider;

import java.util.List;
import java.util.Optional;

import br.com.desafio.compradireta.domain.entity.entities.CategoriaCompra;

public interface ICategoriaCompraDataProvider {

    List<CategoriaCompra> listarTodas();

    Optional<CategoriaCompra> buscarPorId(Long id);
}
