package br.com.desafio.compradireta.domain.usecase.notificacao.listar;

import java.time.LocalDateTime;

import br.com.desafio.compradireta.domain.constant.StatusNotificacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotificacaoFornecedorOutput {

    private Long fornecedorId;
    private String fornecedorNome;
    private LocalDateTime dataEnvio;
    private StatusNotificacao status;
}
