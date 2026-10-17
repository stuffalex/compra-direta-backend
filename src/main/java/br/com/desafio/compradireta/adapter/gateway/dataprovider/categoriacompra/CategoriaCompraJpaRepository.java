package br.com.desafio.compradireta.adapter.gateway.dataprovider.categoriacompra;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio.compradireta.domain.entity.entities.CategoriaCompra;

public interface CategoriaCompraJpaRepository extends JpaRepository<CategoriaCompra, Long> {
}
