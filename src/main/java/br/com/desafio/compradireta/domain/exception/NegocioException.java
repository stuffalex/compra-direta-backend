package br.com.desafio.compradireta.domain.exception;

public class NegocioException extends RuntimeException {

    public NegocioException(String message) {
        super(message);
    }
}
