package br.com.desafio.compradireta.adapter.gateway.outbox;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.desafio.compradireta.domain.constant.StatusOutbox;
import br.com.desafio.compradireta.domain.entity.entities.OutboxEvento;

public interface OutboxJpaRepository extends JpaRepository<OutboxEvento, Long> {

    List<OutboxEvento> findByStatusOrderByCriadoEmAsc(StatusOutbox status, Pageable pageable);
}
