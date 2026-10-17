package br.com.desafio.compradireta.domain.usecase.compradireta.criar;

import java.time.LocalDate;
import java.util.List;

import br.com.desafio.compradireta.domain.constant.CriterioJulgamento;
import br.com.desafio.compradireta.domain.constant.Modalidade;
import br.com.desafio.compradireta.domain.usecase.compradireta.dto.ItemCompraDiretaInput;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriarCompraDiretaInput {

    private String objeto;
    private String justificativa;
    private Modalidade modalidade;
    private CriterioJulgamento criterioJulgamento;
    private Long unidadeCompradoraId;
    private Long categoriaCompraId;
    private LocalDate dataAbertura;
    private LocalDate dataLimitePropostas;
    private List<ItemCompraDiretaInput> itens;
    private List<Long> fornecedoresConvidadosIds;
}
