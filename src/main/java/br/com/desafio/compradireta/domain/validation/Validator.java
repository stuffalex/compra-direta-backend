package br.com.desafio.compradireta.domain.validation;

import br.com.desafio.compradireta.domain.exception.NegocioException;

public final class Validator {

    private Validator() {
    }

    public static void isTrue(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new NegocioException(mensagem);
        }
    }

    public static void notNull(Object valor, String mensagem) {
        isTrue(valor != null, mensagem);
    }

    public static void notBlank(String valor, String mensagem) {
        isTrue(valor != null && !valor.isBlank(), mensagem);
    }
}
