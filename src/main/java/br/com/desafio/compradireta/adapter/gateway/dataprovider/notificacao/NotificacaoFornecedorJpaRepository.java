package br.com.desafio.compradireta.adapter.gateway.dataprovider.notificacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio.compradireta.domain.entity.entities.NotificacaoFornecedor;

public interface NotificacaoFornecedorJpaRepository extends JpaRepository<NotificacaoFornecedor, Long> {

    boolean existsByCompraDiretaIdAndFornecedorId(Long compraDiretaId, Long fornecedorId);

    List<NotificacaoFornecedor> findByCompraDiretaId(Long compraDiretaId);
}
