package br.com.desafio.compradireta.domain.usecase.proposta.registrar;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarPropostaInput {

    private Long compraDiretaId;
    private Long fornecedorId;
    private BigDecimal valorProposto;
    private BigDecimal percentualDesconto;
    private Integer prazoEntregaDias;
}
