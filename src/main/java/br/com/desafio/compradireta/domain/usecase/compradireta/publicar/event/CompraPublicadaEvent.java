package br.com.desafio.compradireta.domain.usecase.compradireta.publicar.event;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraPublicadaEvent implements Serializable {

    private Long compraDiretaId;
    private String numero;
    private List<Long> fornecedoresConvidadosIds;
}
