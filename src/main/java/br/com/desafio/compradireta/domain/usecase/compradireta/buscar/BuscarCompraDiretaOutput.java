package br.com.desafio.compradireta.domain.usecase.compradireta.buscar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.Modalidade;
import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import br.com.desafio.compradireta.domain.constant.TipoFornecedor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BuscarCompraDiretaOutput {

    private Long id;
    private String numero;
    private String objeto;
    private String justificativa;
    private Modalidade modalidade;
    private CriterioJulgamento criterioJulgamento;
    private ReferenciaOutput unidadeCompradora;
    private ReferenciaOutput categoriaCompra;
    private StatusCompraDireta status;
    private LocalDate dataAbertura;
    private LocalDate dataLimitePropostas;
    private BigDecimal valorEstimadoTotal;
    private List<ItemOutput> itens;
    private List<FornecedorResumoOutput> fornecedoresConvidados;
    private List<PropostaResumoOutput> propostas;
    private FornecedorResumoOutput fornecedorVencedor;
    private LocalDateTime dataHomologacao;
    private String motivoCancelamento;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ReferenciaOutput {
        private Long id;
        private String nome;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ItemOutput {
        private Long id;
        private String descricao;
        private String unidadeMedida;
        private BigDecimal quantidade;
        private BigDecimal valorUnitarioEstimado;
        private BigDecimal valorTotalEstimado;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FornecedorResumoOutput {
        private Long id;
        private String razaoSocial;
        private String email;
        private TipoFornecedor tipoFornecedor;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PropostaResumoOutput {
        private Long id;
        private Long fornecedorId;
        private String fornecedorNome;
        private BigDecimal valorProposto;
        private BigDecimal percentualDesconto;
        private Integer prazoEntregaDias;
        private LocalDateTime dataEnvio;
    }
}
