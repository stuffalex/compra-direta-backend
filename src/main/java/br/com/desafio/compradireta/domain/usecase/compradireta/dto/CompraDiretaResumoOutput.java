package br.com.desafio.compradireta.domain.usecase.compradireta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.Modalidade;
import br.com.desafio.compradireta.domain.constant.StatusCompraDireta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CompraDiretaResumoOutput {

    private Long id;
    private String numero;
    private String objeto;
    private Modalidade modalidade;
    private CriterioJulgamento criterioJulgamento;
    private StatusCompraDireta status;
    private BigDecimal valorEstimadoTotal;
    private LocalDate dataAbertura;
    private LocalDate dataLimitePropostas;
}
