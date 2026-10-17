package br.com.desafio.compradireta.domain.entity.entities;

import java.math.BigDecimal;

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
@Table(name = "item_compra_direta")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCompraDireta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "compra_direta_id", nullable = false)
    private CompraDireta compraDireta;

    @Column(nullable = false)
    private String descricao;

    @Column(name = "unidade_medida", nullable = false)
    private String unidadeMedida;

    @Column(nullable = false)
    private BigDecimal quantidade;

    @Column(name = "valor_unitario_estimado", nullable = false)
    private BigDecimal valorUnitarioEstimado;

    public BigDecimal getValorTotalEstimado() {
        return valorUnitarioEstimado.multiply(quantidade);
    }
}
