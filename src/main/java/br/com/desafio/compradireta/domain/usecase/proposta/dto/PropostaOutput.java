package br.com.desafio.compradireta.domain.usecase.proposta.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PropostaOutput {

    private Long id;
    private Long fornecedorId;
    private String fornecedorNome;
    private BigDecimal valorProposto;
    private BigDecimal percentualDesconto;
    private Integer prazoEntregaDias;
    private LocalDateTime dataEnvio;
}
