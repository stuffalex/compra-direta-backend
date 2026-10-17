package br.com.desafio.compradireta.domain.entity.entities;

import java.time.LocalDateTime;

import br.com.desafio.compradireta.domain.constant.StatusNotificacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notificacao_fornecedor")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoFornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "compra_direta_id", nullable = false)
    private CompraDireta compraDireta;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusNotificacao status;
}
