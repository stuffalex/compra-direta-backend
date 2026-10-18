package br.com.desafio.compradireta.domain.pagination;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class Paginacao {

    @NotNull(message = "O número da página não pode ser nulo")
    @Min(value = 0, message = "O número da página deve ser maior ou igual a zero")
    @Builder.Default
    private Integer pagina = 0;

    @NotNull(message = "O tamanho da página não pode ser nulo")
    @Min(value = 1, message = "O tamanho da página deve ser de pelo menos 1")
    @Builder.Default
    private Integer tamanho = 20;

    public static Paginacao of(Integer pagina, Integer tamanho) {
        return Paginacao.builder()
                .pagina(pagina == null || pagina < 0 ? 0 : pagina)
                .tamanho(tamanho == null || tamanho <= 0 ? 20 : tamanho)
                .build();
    }
}
