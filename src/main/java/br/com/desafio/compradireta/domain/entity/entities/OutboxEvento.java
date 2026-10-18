package br.com.desafio.compradireta.domain.entity.entities;

import java.time.LocalDateTime;

import br.com.desafio.compradireta.domain.constant.StatusOutbox;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "outbox_evento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private Long aggregateId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "correlation_id")
    private String correlationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOutbox status;

    @Builder.Default
    @Column(nullable = false)
    private int tentativas = 0;

    @Column(columnDefinition = "TEXT")
    private String erro;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @Column(name = "processado_em")
    private LocalDateTime processadoEm;
}
