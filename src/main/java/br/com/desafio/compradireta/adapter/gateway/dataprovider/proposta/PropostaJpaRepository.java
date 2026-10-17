package br.com.desafio.compradireta.adapter.gateway.dataprovider.proposta;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio.compradireta.domain.entity.entities.Proposta;

public interface PropostaJpaRepository extends JpaRepository<Proposta, Long> {

    List<Proposta> findByCompraDiretaId(Long compraDiretaId);

    Optional<Proposta> findByCompraDiretaIdAndFornecedorId(Long compraDiretaId, Long fornecedorId);
}
