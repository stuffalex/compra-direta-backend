package br.com.desafio.compradireta.domain.pagination;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Paginacao {

    @Builder.Default
    private int pagina = 0;

    @Builder.Default
    private int tamanho = 20;

    public static Paginacao of(int pagina, int tamanho) {
        return Paginacao.builder()
                .pagina(Math.max(0, pagina))
                .tamanho(tamanho <= 0 ? 20 : tamanho)
                .build();
    }
}
