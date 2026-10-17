package br.com.desafio.compradireta.adapter.gateway.dataprovider.compradireta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.desafio.compradireta.domain.entity.entities.CompraDireta;

public interface CompraDiretaJpaRepository extends JpaRepository<CompraDireta, Long> {

    @Query("SELECT COUNT(c) FROM CompraDireta c WHERE YEAR(c.dataAbertura) = :ano")
    long contarPorAno(@Param("ano") int ano);
}
