package br.com.desafio.compradireta.domain.usecase.compradireta.cancelar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CancelarCompraDiretaInput {

    private Long id;
    private String motivo;
}
