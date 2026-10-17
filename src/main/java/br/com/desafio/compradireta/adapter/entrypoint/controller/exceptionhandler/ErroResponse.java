package br.com.desafio.compradireta.adapter.entrypoint.controller.exceptionhandler;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class ErroResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final String erro;
    private final String mensagem;

    public ErroResponse(int status, String erro, String mensagem) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
    }
}
