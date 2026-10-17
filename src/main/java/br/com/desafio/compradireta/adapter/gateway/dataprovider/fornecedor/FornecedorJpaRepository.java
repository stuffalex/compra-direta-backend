package br.com.desafio.compradireta.adapter.gateway.dataprovider.fornecedor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio.compradireta.domain.entity.entities.Fornecedor;

public interface FornecedorJpaRepository extends JpaRepository<Fornecedor, Long> {

    List<Fornecedor> findByIdIn(List<Long> ids);
}
