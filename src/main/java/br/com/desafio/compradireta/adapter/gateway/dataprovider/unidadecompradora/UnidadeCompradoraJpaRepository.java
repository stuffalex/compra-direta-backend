package br.com.desafio.compradireta.adapter.gateway.dataprovider.unidadecompradora;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio.compradireta.domain.entity.entities.UnidadeCompradora;

public interface UnidadeCompradoraJpaRepository extends JpaRepository<UnidadeCompradora, Long> {
}
