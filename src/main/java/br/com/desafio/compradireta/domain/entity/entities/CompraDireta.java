package br.com.desafio.compradireta.domain.entity.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.Modalidade;
import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "compra_direta")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraDireta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(nullable = false)
    private String objeto;

    @Column(nullable = false)
    private String justificativa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Modalidade modalidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "criterio_julgamento", nullable = false)
    private CriterioJulgamento criterioJulgamento;

    @ManyToOne
    @JoinColumn(name = "unidade_compradora_id", nullable = false)
    private UnidadeCompradora unidadeCompradora;

    @ManyToOne
    @JoinColumn(name = "categoria_compra_id", nullable = false)
    private CategoriaCompra categoriaCompra;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCompraDireta status;

    @Column(name = "data_abertura", nullable = false)
    private LocalDate dataAbertura;

    @Column(name = "data_limite_propostas", nullable = false)
    private LocalDate dataLimitePropostas;

    @ManyToOne
    @JoinColumn(name = "fornecedor_vencedor_id")
    private Fornecedor fornecedorVencedor;

    private LocalDateTime dataHomologacao;

    @Column(name = "motivo_cancelamento")
    private String motivoCancelamento;

    @Builder.Default
    @OneToMany(mappedBy = "compraDireta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemCompraDireta> itens = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "compraDireta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CompraDiretaFornecedor> fornecedoresConvidados = new ArrayList<>();

    public BigDecimal getValorEstimadoTotal() {
        return itens.stream()
                .map(ItemCompraDireta::getValorTotalEstimado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
