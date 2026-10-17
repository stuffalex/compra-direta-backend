package br.com.desafio.compradireta.domain.entity.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "proposta")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "compra_direta_id", nullable = false)
    private CompraDireta compraDireta;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(name = "valor_proposto")
    private BigDecimal valorProposto;

    @Column(name = "percentual_desconto")
    private BigDecimal percentualDesconto;

    @Column(name = "prazo_entrega_dias", nullable = false)
    private Integer prazoEntregaDias;

    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;
}
